// var token = $("meta[name='_csrf']").attr("content");
// var header = $("meta[name='_csrf_header']").attr("content");
// $(document).ajaxSend(function (e, xhr, options) {
//   xhr.setRequestHeader(header, token);
//   console.log(e);
// });

// $(function () {
//   $(document).on("click", "[class^=star-]", function () {
//     const inputRating = $(this).next().val();
//     const inputProductId = $(this).next().next().val();

//     $.ajax({
//       type: "POST",
//       contentType: "application/json",
//       url: "rate",
//       data: { rating: inputRating, productId: inputProductId },
//       dataType: "json",
//       success: function (result) {
//         console.log("SUCCESS: ", result);
//       },
//       error: function (e) {
//         console.log("ERROR: ", e);
//       },
//       done: function (e) {
//         console.log("DONE");
//       },
//     });
//   });
// });
