package com.fundooproject.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fundooproject.model.Note;

@Repository
public interface INoteRepository extends JpaRepository<Note, Long> {

	Optional<Note> findById(long noteId);

	//Optional<Note> findByUserId(long userId);
	List<Note> findByUserId(Long userId);
	
	 
	Optional<Note>  findByNoteIdAndUserId(long noteId,long userId);

}
