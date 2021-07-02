// var token = $("meta[name='_csrf']").attr("content");
// var header = $("meta[name='_csrf_header']").attr("content");
// $(document).ajaxSend(function (e, xhr, options) {
//   xhr.setRequestHeader(header, token);
//   console.log(e);
// });

// $(document).ready(function () {
//   $(".add-to-cart").click(function (e) {
//     const inputQuantity = $(this).prev().val();
//     const id = $(this).val();
//     console.log(inputQuantity);

//     $.ajax({
//       url: "/addproduct",
//       type: "POST",
//       data: {
//         productId: id,
//         productQuantity: inputQuantity,
//       },
//     });
//   });
// });
