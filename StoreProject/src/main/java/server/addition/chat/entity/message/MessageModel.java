package server.addition.chat.entity.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.addition.chat.entity.room.RoomModel;
import server.addition.chat.entity.state.MessageState;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class MessageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private String text;

    @Transient
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "message")
    private Set<MessageState> messageStates;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room")
    private RoomModel room;
}
