// var token = $("meta[name='_csrf']").attr("content");
// var header = $("meta[name='_csrf_header']").attr("content");
// $(document).ajaxSend(function (e, xhr, options) {
//   xhr.setRequestHeader(header, token);
//   console.log(e);
// });

$(function () {
  var prevRatingInput = null;

  $(".btn-edit, .btn-delete, .btn-add, .btn-product-details").click(
    function () {
      const form = $(this).prev();

      form.css("display", "grid");
      form.find(".exit-modal").css("margin-left", "auto");
    }
  );

  $(".exit-modal").click(function () {
    const form = $(this).parent().parent();

    form.css("display", "none");
  });

  $(".exit-modal-add-product").click(function () {
    const form = $(this).parent();

    form.css("display", "none");
  });

  $(".btn-rating").click(function () {
    const input = $(this).prev();

    if (prevRatingInput != null) {
      $(prevRatingInput).prop("checked", false);
      $(prevRatingInput).next().css("background-color", "transparent");
    }

    $(this).css("background-color", "aquamarine");

    prevRatingInput = input;
  });
});
