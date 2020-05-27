package com.fundooproject.service;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fundooproject.model.User;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.fundooproject.configuration.Response;
import com.fundooproject.dto.LabelDto;
import com.fundooproject.exception.NoteException;
import com.fundooproject.exception.ResponseHelper;
import com.fundooproject.model.Label;
import com.fundooproject.model.Note;
import com.fundooproject.repository.ILabelRepository;
import com.fundooproject.repository.INoteRepository;
import com.fundooproject.repository.IUserRepository;
import com.fundooproject.utility.JwtTokenUtil;

@Service
public class LabelServiceImp implements ILabelService {

	@Autowired
	private ILabelRepository labelRepository;

	@Autowired
	private JwtTokenUtil tokenGenerator;

	@Autowired
	private INoteRepository noteRepository;
	
	@Autowired
	private IUserRepository userRepository;


	@Autowired
	private Environment environment;

	
	@Override
	public Response addLabelToNote(String token,LabelDto labelDto, long noteId) {
		long userId = tokenGenerator.decodeToken(token);
	    String checkLabel = labelDto.getLabelName();
		Optional<Note> note = noteRepository.findById(noteId);
		Optional<User> user = userRepository.findById(userId);
		Optional<Label> label1 = labelRepository.findBylabelNameAndNoteId(checkLabel, note.get().getNoteId());
        if(user.isPresent()) {
        	if(note!=null)
		if (label1.isPresent()) {
			return ResponseHelper.statusResponse(501, environment.getProperty("status.label.exist"), checkLabel);
		}

		ModelMapper mapper = new ModelMapper();
		Label label = mapper.map(labelDto, Label.class);
		LocalDateTime localDateTime = LocalDateTime.now();
		label.setCreDate(localDateTime);
        label.setNoteId(note.get().getNoteId());
		note.get().getLabels().add(label);
		noteRepository.save(note.get());
		return ResponseHelper.statusResponse(200, environment.getProperty("status.label.createdSuccessfull"),
				checkLabel);
	}else {
		System.out.println("User doesn't exist ");
	}
	return ResponseHelper.statusResponse(200, environment.getProperty("status.label.userNotExist"),
			"noteId - " + noteId);
	
	}
	

	@Override
	public Response deleteLabel(String token,long labelId) throws NoteException {
		long userId = tokenGenerator.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);
	
	    Optional<Label> label = labelRepository.findById(labelId);
		label.orElseThrow(() -> new NoteException(environment.getProperty("status.label.notexist")));
		if(user.isPresent()) {
		if (label != null) {
			labelRepository.deleteById(labelId);
		}
		return ResponseHelper.statusResponse(200, environment.getProperty("status.label.deleted"), label);
	}else {
		System.out.println("User doesn't exist ");
	}
	return ResponseHelper.statusResponse(200, environment.getProperty("status.label.notDeleted"),
			"labelId - " + labelId);
	}

	@Override
	public Response updateLabel(String token,LabelDto labelDto, long labelId) throws NoteException {
		long userId = tokenGenerator.decodeToken(token);
		Optional<Label> label = labelRepository.findById(labelId);
		Optional<User> user = userRepository.findById(userId);
		label.orElseThrow(() -> new NoteException(environment.getProperty("status.label.notexist")));
		LocalDateTime localDateTime = LocalDateTime.now();
		 try {
        	if(user.isPresent()) {
        		if(label!=null)
        	
			label.get().setLabelName(labelDto.getLabelName());
        	label.get().setModDate(localDateTime);
            
			labelRepository.save(label.get());
			return ResponseHelper.statusResponse(200, environment.getProperty("status.note.updatedSuccessfull"),
					labelDto.getLabelName());

        }
		} catch (Exception e) {
			e.printStackTrace();
		}
        return ResponseHelper.statusResponse(200, environment.getProperty("status.label.userNotExist"),
    			"labelId - " + labelId);
    	
	}
		
	@Override
	public List<Label> getAllLabel(String token,long noteId) {
		//long userId = tokenGenerator.decodeToken(token);
	//	Optional<User> user = userRepository.findById(userId);
	
		return labelRepository.findByNoteId(noteId);
	}

	@Override
	public Response mapLabelToNote(String token,long noteId, long labelId) throws NoteException {
		Optional<Note> note = noteRepository.findById(noteId);
		note.orElseThrow(() -> new NoteException(environment.getProperty("status.note.notExist")));
		Optional<Label> label = labelRepository.findById(labelId);
		label.orElseThrow(() -> new NoteException(environment.getProperty("status.label.notexist")));
		List<Label> labelsList = note.get().getLabels();
		for (Label label2 : labelsList) {
			if (label2.getLabelId() == labelId) {
				throw new NoteException("Label already exist");
			}
		}
		note.get().getLabels().add(label.get());
		noteRepository.save(note.get());
		return ResponseHelper.statusResponse(200, environment.getProperty("status.label.mapped"),
				noteRepository.findById(noteId));
	}

	private <T> Response buildSuccessResponse(T t) {
		return ResponseHelper.statusResponse(200, environment.getProperty("status.label.updated"), t);
	}

}
