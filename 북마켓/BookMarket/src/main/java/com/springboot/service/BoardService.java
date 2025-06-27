package com.springboot.service;

import com.springboot.domain.Board;
import com.springboot.domain.BoardFormDto;
import com.springboot.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    @Transactional
    public Long savePost(BoardFormDto boardDto){
        return boardRepository.save(boardDto.toEntity()).getId();
    }

    @Transactional
    public List<BoardFormDto> getBoardList(){
        List<Board> boardList = boardRepository.findAll();
        List<BoardFormDto> boardDtoList = new ArrayList<>();
        for(Board board : boardList){
            BoardFormDto boardDto = BoardFormDto.builder().id(board.getId()).writerid(board.getWriterid())
                    .writer(board.getWriter()).title(board.getTitle()).content(board.getContent())
                    .createdDate(board.getCreatedDate()).build();
            boardDtoList.add(boardDto);
        }
        return boardDtoList;
    }

    // 아이디로 찾기
    public Board getBoardById(Long id){
        Board board = boardRepository.findById(id).get();
        // Optional.get() : 내부에 실제 객체가 있을 때 꺼내는 메서드. 없으면 NoSuchElementException 예외 발생
        return board;
    }

    // 기존 방식
//    public Board getBoardById(Long id) {
//        return boardRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
//    }

    // 삭제하기
    public void deleteBoardById(Long id){
        boardRepository.deleteById(id);
    }

    public Page<Board> listAll(int pageNum, String sortField, String sortDir){
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNum-1, pageSize, sortDir.equals("asc")?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending());
        return boardRepository.findAll(pageable);
        // pageNum : 요청하는 페이지 번호(1부터 시작)
        // sortField : 정렬한 컬럼명
        // sortDir : 정렬 방향. asc, desc
        // pageRequest.of : 페이지 요청정보를 만들는 메서드
        // pageNum-1 : data jpa는 0부터 페이지번호를 계산하기 때문에 pageNum에서 1을 뺌
        // pageNum -1  : data jpa 0부터 페이지번호 셈해서 pageNum 에서 1을 뺌
        // pageSize : 한 페이지에 몇 개의 항목을 가져올지
        // Sort.by(sortField) :   sortField 기준정렬
        // sortDir.equals("asc") 체크 후 오름차순인지 내림차순인지 설정


    }



}
