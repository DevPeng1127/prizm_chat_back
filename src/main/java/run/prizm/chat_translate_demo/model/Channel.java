package run.prizm.chat_translate_demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "channels")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Channel {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long workspaceId;

    private Long categoryId;

    @Column(nullable = false)
    private String type; // Assuming ChannelType entity/enum exists

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(precision = 20, scale = 10)
    private BigDecimal zIndex;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}