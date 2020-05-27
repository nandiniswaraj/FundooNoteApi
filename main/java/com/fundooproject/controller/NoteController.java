package com.fundooproject.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;

import com.fundooproject.configuration.Response;
import com.fundooproject.dto.NoteDto;
import com.fundooproject.model.Note;
import com.fundooproject.service.INoteService;

@RequestMapping(value = "/note")
@RestController
public class NoteController {

	@Autowired
	private INoteService noteService;

	@PostMapping("/createNote")
	@ApiOperation("Create Note.")
	public Response noteCreation(@RequestBody NoteDto noteDto, @RequestParam(value="token") String token) {
		return noteService.createNote(noteDto, token);
	}

    @DeleteMapping("/deleteNote/{noteId}")
	@ApiOperation("Delete note.")
	public Response deleteNote(@RequestHeader String token,@PathVariable long noteId) {
		return noteService.deleteNote(token,noteId);
	}

	@PutMapping("/updateColor/{color}/{noteId}")
	@ApiOperation( "Color update.")
	public Response colorUpdate(@RequestHeader String token,@PathVariable String color, @PathVariable Long noteId) {
		return noteService.colorUpdate(token,noteId, color);
	}

	
	@PutMapping("/reminder/{noteId}/{reminder}")
	@ApiOperation( "Add reminder.")
	public Response setReminder(@RequestHeader String token,@PathVariable String reminder, @PathVariable Long noteId) throws Exception {
		return noteService.setReminder(token,noteId, reminder);
	}

	@GetMapping("/getAllNotes")
	@ApiOperation(value = "Get all notes.")
	public List<Note> getAll(@RequestHeader String token) throws Exception {
		return noteService.getAllNotes(token);
	}
	
	@GetMapping("/findAllNotesByTitles")
	@ApiOperation(value = "Get all notes based on title")
	public List<Note> getAllNotesByTitle(@RequestHeader String token,String titles) throws Exception {
		return noteService.findByTitle(token,titles);
	}
	@GetMapping("/findAllNotesByDescription")
	@ApiOperation(value = "Get all notes based on Description")
	public List<Note> getAllNotesByDescription(@RequestHeader String token,String titles) throws Exception {
		return noteService.findBydescription(token,titles);
	}



	@PostMapping("/setCollaborator")
	public Response  setCollaborator(@RequestParam String token, @RequestParam Long noteId,@RequestParam String email) {
       return noteService.addCollaborator(token, noteId, email);
	}
	
	@PostMapping("/deleteCollaborator")
	public Response deleteCollaborator(@RequestParam String token, @RequestParam Long noteId, @RequestParam String email) {

	return noteService.deleteCollaborator(token, noteId, email);
	}


	
	@PutMapping("dataUpdate/{noteId}")
	@ApiOperation( " Data update.")
	public Response updateData(@RequestHeader String token,@RequestBody NoteDto noteDto, @PathVariable Long noteId) {
		return noteService.dataUpdate(token,noteId, noteDto);
	}
	 @PostMapping("/setTrash")
	 public Response setTrash( @RequestParam Long noteId) {

	 return noteService.trashNote(noteId);
	 
	 }
	 @PostMapping("/restoreFromTrash")
	 public Response restoreFromTrash(@RequestParam Long noteId) {

	  return noteService.restoreFromTrash(noteId);
	
	 }
	  @PutMapping(value = "/pinUnpinNote")
	 public Response pinUnpinNote(@RequestParam String token, @RequestParam Long noteId) {
         return noteService.pinUnpin(token, noteId);
	 }

	 @PutMapping(value = "/archive")
	 public Response archive(@RequestParam String token, @RequestParam Long noteId) {
      return noteService.archive(token, noteId);
	 
	 }



}