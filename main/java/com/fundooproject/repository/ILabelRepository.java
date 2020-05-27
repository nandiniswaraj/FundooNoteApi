package com.fundooproject.repository;

import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fundooproject.model.Label;

@Repository
public interface ILabelRepository extends JpaRepository<Label, Long> {

	Optional<Label> findBylabelNameAndNoteId(String labelName,Long noteId);

	//List<Label> findByUserId(long userId);
	
	List<Label> findByNoteId(long noteId);
	
	

}
