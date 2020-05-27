package com.fundooproject.controller;

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

import com.fundooproject.configuration.Response;
import com.fundooproject.dto.LabelDto;
import com.fundooproject.exception.NoteException;
import com.fundooproject.model.Label;
import com.fundooproject.service.ILabelService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/label")
public class LabelController {

	@Autowired
	private ILabelService labelService;

	/*@PostMapping("/addlabel")
	@ApiOperation(value = "Add label")
	public Response labelByUser(@RequestHeader String token,@RequestBody LabelDto labelDto) {
		return labelService.addLabel(token,labelDto);
	}*/

	@PostMapping("/addLabelToNote")
	@ApiOperation(value = "Add label by note ")
	public Response addLabelToNote(@RequestHeader String token,@RequestBody LabelDto labelDto, @RequestParam long noteId) {
		return labelService.addLabelToNote(token,labelDto, noteId);
	}
	

	@PutMapping("/{noteId}/{labelId}")
	@ApiOperation(value = "Map label")
	public Response mapLabel(@RequestHeader String token,@PathVariable long noteId, @PathVariable long labelId) throws NoteException {
		return labelService.mapLabelToNote(token,noteId, labelId);
	}

	@DeleteMapping("/deleteLabel/{labelId}")
	@ApiOperation(value = "Delete label")
	public Response deleteLabel(@RequestHeader String token,@PathVariable long labelId) throws NoteException {
		return labelService.deleteLabel(token,labelId);
	}

	@PutMapping("/updateLabel/{labelId}")
	@ApiOperation(value = "Update label")
	public Response updateLabel(@RequestHeader String token,@RequestBody LabelDto labelDto, @PathVariable long labelId) throws NoteException {
		return labelService.updateLabel(token,labelDto, labelId);
	}

	@GetMapping("/{userId}")
	@ApiOperation(value = "Label list ")
	public List<Label> getAllLabel(@RequestHeader String token,@PathVariable long userId) {
		return labelService.getAllLabel(token,userId);
	}
}
