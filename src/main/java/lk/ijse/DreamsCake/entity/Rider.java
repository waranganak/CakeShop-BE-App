package lk.ijse.DreamsCake.entity;

import jakarta.persistence.*;
import lk.ijse.DreamsCake.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "riders")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Rider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String phone;
    private String vehicleNumber;
    private String status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role = RoleType.RIDER;

    @OneToMany(mappedBy = "rider")
    private List<Delivery> deliveries;

}