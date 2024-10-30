package vn.iotstar.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Data
public class categoryModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "categoryid")
	private Long id;
	@NotEmpty(message = "Không được bỏ trống")
	private String name;
	private String image;
	private int status;
	private boolean isEdit=false;
}
