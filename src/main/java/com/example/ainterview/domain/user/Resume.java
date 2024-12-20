package com.example.ainterview.domain.user;

import org.hibernate.mapping.ToOne;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resume {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	@Lob
	private String academicAbility;
	@Lob
	private String career;
	@Lob
	private String contact;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Override
	public String toString() {
		return "Resume{" +
			"id=" + id +
			", name='" + name + '\'' +
			", academicAbility='" + academicAbility + '\'' +
			", career='" + career + '\'' +
			", contact='" + contact + '\'' +
			", user=" + user +
			'}';
	}
}
