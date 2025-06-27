package loginBoard.board2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id @GeneratedValue
    private Long id;
    private String username;
    private String password;

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    // 회원이 작성한 글
    @OneToMany(mappedBy = "writer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();
    // 멤버를 삭제할 때 해당 멤버가 작성한 모든 글도 같이 삭제됨
    // user는 한 명이고, 여러 보드를 가질 수 있음.
    // 보드는 하나의 유저(writer)만 가리킴.
    // 보드가 연관 관계의 주인이고, 유저는 mappedBy로 관계만 나타냄.
}
