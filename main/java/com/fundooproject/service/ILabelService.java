package com.fundooproject.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fundooproject.configuration.Response;
import com.fundooproject.dto.LabelDto;
import com.fundooproject.exception.NoteException;
import com.fundooproject.model.Label;

@Service
public interface ILabelService {

	//Response addLabel(String token,LabelDto labelDto);

	Response addLabelToNote(String token,LabelDto labelDto, long noteId);

	Response deleteLabel(String token,long labelId) throws NoteException;

	Response updateLabel(String token,LabelDto labelDto, long labelId) throws NoteException;

	//List<Label> getAllLabel(long userId);
	List<Label> getAllLabel(String token,long noteId);


	Response mapLabelToNote(String token,long noteId, long labelId) throws NoteException;

}
