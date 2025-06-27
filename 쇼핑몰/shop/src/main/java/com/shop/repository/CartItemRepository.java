package com.shop.repository;

import com.shop.dto.CartDetailDto;
import com.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    @Query("select new com.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.repImgYn = 'Y' " +
            "order by ci.regTime desc"
    )
    List<CartDetailDto> findCartDetailDtoList(Long cartId);
}
// 특정 장바구니 id에 담긴 상품목록을 조회하면서 각 상품의 대표이미지를 함께 가져옴
// cartItem <-> ItemImg 직접 연관 없음
// cartItem -> Item <- ItemImg
// 즉 둘 다 Item 을 기준으로 간접 연결
// 'Join ci.item i' 로 상품을 조인
// 이미지(itemImg)는 따로 나열한 후 where 절에서 조건으로 연결

//    @Query("SELECT new com.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
//            "FROM CartItem ci " +
//            "JOIN ci.item i " +
//            "JOIN ItemImg im ON im.item.id = i.id " +
//            "WHERE ci.cart.id = :cartId " +
//            "AND im.repimgYn = 'Y' " +
//            "ORDER BY ci.regTime DESC")
//    List<CartDetailDto> findCartDetailDtoList(@Param("cartId") Long cartId);

//@Query("select ci from CartItem ci " +
//        "join fetch ci.item i " +
//        "where ci.cart.id = :cartId " +
//        "order by ci.regTime desc")
//List<CartItem> findCartItemsWithItem(@Param("cartId") Long cartId);


//@Transactional(readOnly = true)
//public List<CartDetailDto> getCartDetailList(String email) {
//    Member member = memberRepository.findByEmail(email)
//            .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
//
//    Cart cart = cartRepository.findByMemberId(member.getId());
//
//    if (cart == null) {
//        return Collections.emptyList();
//    }
//
//    List<CartItem> cartItems = cartItemRepository.findCartItemsWithItem(cart.getId());
//
//    return cartItems.stream()
//            .map(ci -> {
//                // 대표 이미지 가져오기
//                ItemImg repImg = itemImgRepository.findFirstByItemIdAndRepimgYn(ci.getItem().getId(), "Y");
//                return new CartDetailDto(
//                        ci.getId(),
//                        ci.getItem().getItemNm(),
//                        ci.getItem().getPrice(),
//                        ci.getCount(),
//                        repImg != null ? repImg.getImgUrl() : null
//                );
//            })
//            .collect(Collectors.toList());
//}
