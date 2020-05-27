package com.fundooproject.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fundooproject.repository.IUserRepository;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fundooproject.configuration.Response;
import com.fundooproject.dto.NoteDto;
import com.fundooproject.dto.RabbitMqDto;
import com.fundooproject.exception.NoteException;
import com.fundooproject.exception.ResponseHelper;
import com.fundooproject.exception.UserException;
//import com.fundooproject.model.Collaborator;
import com.fundooproject.model.Note;
import com.fundooproject.model.User;
//import com.fundooproject.repository.ICollabRepository;
import com.fundooproject.repository.INoteRepository;
import com.fundooproject.utility.RedisTempl;
import com.fundooproject.utility.JwtTokenUtil;
import com.fundooproject.utility.RabbitMqImp;

@PropertySource(value = { "classpath:response.properties" })
@Service
public class NoteServiceImp implements INoteService {

	@Autowired
	private INoteRepository noteRepository;
	
	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private JwtTokenUtil tokenGenerator;

	@Autowired
	private Environment environment;

	@Autowired
	private RedisTempl redis;
	
	@Autowired
	private RabbitMqDto rabbitMqDto;

	@Autowired
	private RabbitMqImp rabbitMq;


	@Autowired
	private RestTemplate restTemplate;

	private String redisKey = "Key";

	@Override
	public Response createNote(NoteDto noteDto, String token) {
		Long userId = tokenGenerator.decodeToken(token);
		if (noteDTOValidator(noteDto)) {
			throw new NoteException(environment.getProperty("status.note.validate"));
		}
		try {
		      redis.getMap(redisKey, token);
			ModelMapper mapper = new ModelMapper();
			Note note = mapper.map(noteDto, Note.class);
			LocalDateTime localDateTime = LocalDateTime.now();
			//DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			note.setNoteCreDate(localDateTime);
            note.setUserId(userId);
			note.setNoteColor("white");
			note.setIsArchived("UnArchive");
			note.setIsPinned("Unpin");
			noteRepository.save(note);
			return ResponseHelper.statusResponse(200, environment.getProperty("status.note.createdSuccessfull"),
					noteDto.getTitle());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseHelper.statusResponse(200, environment.getProperty("status.note.validate"),
				noteDto.getTitle());
	}


	private boolean noteDTOValidator(NoteDto noteDto) {
		return (noteDto.getTitle() == null || noteDto.getDescription() == null)
				|| (noteDto.getTitle() == "" && noteDto.getDescription() == "");
	}

	@Override
	public Response deleteNote(String token,long noteId) {
		long userId = tokenGenerator.decodeToken(token);
		Optional<Note> note = noteRepository.findById(noteId);
		Optional<User> user = userRepository.findById(userId);
		note.orElseThrow(() -> new NoteException(environment.getProperty("status.note.deleteError")));
		if(user.isPresent()){
		if (note != null) 
			noteRepository.deleteById(noteId);
			return ResponseHelper.statusResponse(200, environment.getProperty("status.note.deleted"),
					"noteId - " + noteId);
		}else {
			System.out.println("Note doesn't exist ");
		}
		return ResponseHelper.statusResponse(200, environment.getProperty("status.note.notDeleted"),
				"noteId - " + noteId);
	
	}

	@Override
	public List<Note> getAllNotes(String token) {
	Long userId = tokenGenerator.decodeToken(token);
	List<Note> getAllNote = noteRepository.findByUserId(userId);
	return getAllNote;
	}


	@Override
	public Response dataUpdate(String token,Long noteId, NoteDto noteDto) {
		long userId = tokenGenerator.decodeToken(token);
		LocalDateTime localDateTime = LocalDateTime.now();
		Optional<Note> note = noteRepository.findById(noteId);
		Optional<User> user = userRepository.findById(userId);
		note.orElseThrow(() -> new NoteException(environment.getProperty("status.note.notExist")));
		 try {
        	if(user.isPresent()) {
        		if(note!=null)
        			
        		    note.get().setNoteModDate(localDateTime);
                    note.get().setNoteTitle(noteDto.getTitle());
			        note.get().setNoteDescription(noteDto.getDescription());

			   noteRepository.save(note.get());
			return ResponseHelper.statusResponse(200, environment.getProperty("status.note.updatedSuccessfull"),
					noteDto.getTitle());

        }
		} catch (Exception e) {
			e.printStackTrace();
		}
        throw new NoteException(environment.getProperty("status.note.notUpdated"));
        
			}

	@Override
	public Response colorUpdate(String token,Long noteId, String color) {
		long userId = tokenGenerator.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);
		String[] colorArray = { "red", "green", "blue", "white", "pink", "brown", "grey", "yellow", "teal", "dark blue",
				"purple", "orange" };
		Optional<Note> note = noteRepository.findById(noteId);
		note.orElseThrow(() -> new NoteException(environment.getProperty("status.note.notExist")));
		String colour = color.toLowerCase();
		if(user.isPresent()) {
			if(note!=null)
		for (String string : colorArray) {
			if (string.equals(colour)) {
				note.get().setNoteColor(colour);
				noteRepository.save(note.get());
				try {
				} catch (Exception e) {
					e.printStackTrace();
				}
				return ResponseHelper.statusResponse(200, environment.getProperty("status.note.updatedColor"), color);
			}
		}
		}
		return ResponseHelper.statusResponse(200, environment.getProperty("status.note.colorNotPresent"), colorArray);
	}

	@Override
	public Response setReminder(String token,Long noteId, String reminder) throws Exception {
		long userId = tokenGenerator.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);
		Optional<Note> note = noteRepository.findById(noteId);
		note.orElseThrow(() -> new NoteException(environment.getProperty("status.note.notExist")));
		String remender = reminder.toLowerCase();
		LocalDateTime today = LocalDateTime.now();
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:a");
		LocalDateTime tomorrow = today.plusDays(1);
    	LocalDateTime nextWeek = today.plusWeeks(1);
     	String[] reminderOptions = { "today", "tomorrow", "nextweek" };
		if(user.isPresent()) {
    	   if(note!=null)
		for (@SuppressWarnings("unused")
		String string : reminderOptions) {
			if (remender.equals("today")) {
				note.get().setNoteReminder(today.format(dateFormat));
			       
				noteRepository.save(note.get());
				return ResponseHelper.statusResponse(200, environment.getProperty("status.note.todayRem"),
						"Reminder set for today");
			} else if (remender.equals("tomorrow")) {
				note.get().setNoteReminder(tomorrow.format(dateFormat));
		           
				noteRepository.save(note.get());
				return ResponseHelper.statusResponse(200, environment.getProperty("status.note.tomorrowRem"),
						"Reminder set for tomorrow");
			} else if (remender.equals("nextweek")) {
				note.get().setNoteReminder(nextWeek.format(dateFormat));
			       
				noteRepository.save(note.get());
				return ResponseHelper.statusResponse(200, environment.getProperty("status.note.nextWeekRem"),
						"Reminder set for next week");
			}
		    else {
				throw new NoteException("please enter valid reminder day- { Today, Tomorrow, NextWeek }");
			}
		}
		}
		return ResponseHelper.statusResponse(500, environment.getProperty("status.note.remError"), reminderOptions);
	}

	
	 @Override
	 public Response pinUnpin(String token, Long noteId) {

	 Long userId = tokenGenerator.decodeToken(token);
	 Optional<Note> pinData = noteRepository.findByNoteIdAndUserId(noteId, userId);

	 String value = pinData.get().getIsPinned();

	 if (value.equals("Unpin")) {
	 pinData.get().setIsPinned("Pin");
	 noteRepository.save(pinData.get());
	 return ResponseHelper.statusResponse(200, environment.getProperty("note.pin.update"), pinData);
		
	 }
	 return ResponseHelper.statusResponse(200, environment.getProperty("note.already.pinned"), pinData);

	 }

	 @Override
	 public Response trashNote(Long id) {

	 Optional<Note> deleteData = noteRepository.findById(id);

	 deleteData.get().setIsTrashed("Trashed");
	 noteRepository.save(deleteData.get());
	 return ResponseHelper.statusResponse(200, environment.getProperty("note.trashed"),"");
		
	 }

	 @Override
	 public Response restoreFromTrash(Long id) {

	 Optional<Note> deleteData = noteRepository.findById(id);

	 deleteData.get().setIsTrashed("UnTrashed");
	 noteRepository.save(deleteData.get());
	 return ResponseHelper.statusResponse(200, environment.getProperty("note.restore"),"");
	 }


	 @Override
	 public Response archive(String token, Long noteId) {

	 Long userId = tokenGenerator.decodeToken(token);
	 Optional<Note > archiveNote = noteRepository.findByNoteIdAndUserId(noteId, userId);

	 String value = archiveNote.get().getIsArchived();

	 if (value.equals("UnArchive")) {
	 archiveNote.get().setIsArchived("Archive");
	 noteRepository.save(archiveNote.get());
	 return ResponseHelper.statusResponse(200, environment.getProperty("note.set.archived"),"");

	 }
	 return ResponseHelper.statusResponse(200, environment.getProperty("note.already.archived"),"");
     }
	 
	 @Override
		public List<Note> findByTitle(String token, String title)  {
			Long userId = tokenGenerator.decodeToken(token);
			List<Note> getNotes = noteRepository.findByUserId(userId);
				List<Note>list = getNotes.stream().filter(note ->  
				note.getNoteTitle().contains(title)).
				collect(Collectors.toList());
          return list;
		}

		@Override
		public List findBydescription(String token, String description) throws NoteException {

			Long userId = tokenGenerator.decodeToken(token);
			List<Note> getNotes = noteRepository.findByUserId(userId);
				List<Note>list = getNotes.stream().filter(note ->  
				note.getNoteDescription().contains(description)).
				collect(Collectors.toList());
            return list;

	}
		
		

	@Override
	public Response addCollaborator(String token, Long noteId, String email) {

	Long userId = tokenGenerator.decodeToken(token);
	Optional<Note> note = noteRepository.findByNoteIdAndUserId(noteId, userId);
	System.out.println("note is" + note);
	Optional<User> emailList = userRepository.findByEmail(email);

	if (emailList.isPresent()) {

	note.get().getCollaborator().add(emailList.get());
	noteRepository.save(note.get());
	  userRepository.findByEmail(email)
             .map(user -> {
                 String tokenforsending = tokenGenerator.createToken(user.getUser_id());
                 String body =   tokenforsending ;
                 rabbitMqDto.setTo(email);
                 
                 rabbitMqDto.setFrom("nandiniswaraj95@gmail.com");
                 rabbitMqDto.setSubject("Collaboration Invitation");
                 rabbitMqDto.setBody(body);
                 rabbitMq.sendMessageToQueue(rabbitMqDto);
                 rabbitMq.send(rabbitMqDto);
                 return user.getUser_id();
             })
             .orElseThrow(() -> new UserException(UserException.exceptionType.INVALID_EMAIL_ID));
	return ResponseHelper.statusResponse(200, "collaborate user", note);
	}
	return ResponseHelper.statusResponse(200, "collaborate not exist", note);
}
	
	 @Override
	 public Response deleteCollaborator(String token, Long noteId, String email) {

	 Long userId = tokenGenerator.decodeToken(token);
	 Optional<User> emailList = userRepository.findByEmail(email);

	 Optional<Note> collaboraterList = noteRepository.findByNoteIdAndUserId(noteId, userId);

	 collaboraterList.get().getCollaborator().remove(emailList.get());
	 noteRepository.save(collaboraterList.get());

	 return ResponseHelper.statusResponse(200, "collaborate Deleted", collaboraterList);
		 }



}
