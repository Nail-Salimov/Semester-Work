package server.addition.chat.entity.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.addition.chat.entity.link.LinkModel;
import server.addition.chat.entity.message.MessageModel;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class RoomModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER)
    private Set<LinkModel> linkModels;

    @Transient
    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER)
    private Set<MessageModel> messages;
}
