package loginBoard.board2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;

    //회원이 작성한 글
    @OneToMany(mappedBy = "writer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();
    //Member를 삭제할 때 해당 Member가 작성한 모든 Board도 같이 삭제됨

// user 는 한명이고 여러 보드를 가질수 있다다.
//board는 하나의 User만(writer)를 가집니다.
//board가 연관관계의 주인과 User는 mappedBy로 관계만 나타냄


    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Board> getBoards() {
        return boards;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +

                '}';
    }
}
