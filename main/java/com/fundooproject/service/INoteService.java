package com.fundooproject.service;

import org.springframework.stereotype.Service;
import java.util.List;
import com.fundooproject.configuration.Response;
import com.fundooproject.dto.NoteDto;
import com.fundooproject.exception.NoteException;
import com.fundooproject.model.Note;


@Service
public interface INoteService {

	public Response createNote(NoteDto noteDto, String token);

	public Response deleteNote(String token,long noteId);

	public List<Note> getAllNotes(String token) throws Exception;
	
    public Response dataUpdate(String token,Long noteId, NoteDto noteDto);

	public Response colorUpdate(String token,Long noteId, String color);

	public Response setReminder(String token,Long noteId, String reminder) throws Exception;

	Response addCollaborator(String token, Long noteId, String email);

	Response deleteCollaborator(String token, Long noteId, String email);

	Response pinUnpin(String token, Long noteId);

	Response trashNote(Long id);

	Response restoreFromTrash(Long id);

	Response archive(String token, Long noteId);

	List findByTitle(String token, String title) ;

	List findBydescription(String token, String description) throws NoteException;


}
