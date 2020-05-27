package com.fundooproject.model;
import java.io.Serializable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "NOTEINFO")
public class Note implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long noteId;

	@Column(name = "NOTETITLE")
	private String noteTitle;

	@Column(name = "NOTEDESCRIPTION")
	private String noteDescription;

	@Column(name = "NOTECREATEDATE")
	private LocalDateTime noteCreDate;

	@Column(name = "USERID")
	private long userId;
	
	@Column(name = "ISARCHIVED")
    private String isArchived;
	
	@Column(name = "ISPINUNPIN")
	private String isPinned;
	
	@Column(name = "ISTRASHED")
    private String isTrashed="false";
	@Column(name = "NOTECOLOUR")
	private String noteColor;

	@Column(name = "NOTEREMINDER")
	private String noteReminder;
	
	@Column(name = "NOTEMODIFYDATE")
	private LocalDateTime noteModDate;

	@OneToMany(cascade = CascadeType.ALL)
	private List<User> collaborator;

	@ManyToMany(cascade = CascadeType.ALL)
	@Column(name = "NOTEID")
	private List<Label> labels = new ArrayList<>();



	


	
}