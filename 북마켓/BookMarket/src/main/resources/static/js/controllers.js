function addToCart(bookid){
    if(confirm("장바구니에 도서를 추가하시겠습니까?") == true){
        document.addForm.action="/BookMarket/cart/book/"+bookid;
        document.addForm.submit();
    }
}

// 장바구니에 등록된 도서 항목 삭제
function removeFromCart(bookid, cartId){
    if(confirm("장바구니에서 도서를 삭제하시겠습니까?") == true){
        document.removeForm.action="/BookMarket/cart/book/"+bookid;
        document.removeForm.submit();
        setTimeout('location.reload()', 10);    // 0.001초 뒤에 한 번만 새로고침
    }
}

function clearCart(cartId) {
   if (confirm("모든 도서를 장바구니에서 삭제합니까?")==true){

      document.clearForm.submit();
      setTimeout('location.reload()',10);

   }
}