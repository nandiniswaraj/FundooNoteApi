package com.fundooproject.model;

import java.io.Serializable;
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
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "LABELINFO")
public class Label implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	private long labelId;

	@Column(name = "LABELNAME")
	private String labelName;
	
	@Column(name = "LABELCREATEDATE")
	private LocalDateTime creDate;
	
	@Column(name = "LABELMODIFYDATE")
	private LocalDateTime modDate;


	@Column(name = "NOTEID")
	private long noteId;
	

	@ManyToMany(cascade = CascadeType.ALL)
	@Column(name = "NOTEID")
	private List<Note> notes = new ArrayList<>();

	
}
