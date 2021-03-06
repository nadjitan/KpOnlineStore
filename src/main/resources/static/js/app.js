// var token = $("meta[name='_csrf']").attr("content");
// var header = $("meta[name='_csrf_header']").attr("content");
// $(document).ajaxSend(function (e, xhr, options) {
//   xhr.setRequestHeader(header, token);
//   console.log(e);
// });

document.addEventListener("DOMContentLoaded", () => {
  if (document.location.pathname.indexOf("/checkout") == 0) {
    $("#checkbox-useCc").click(function () {
      const input = $(this);

      if (input.is(":checked")) {
        $(".form-cc").hide();
      } else {
        $(".form-cc").show();
      }
    });

    $(
      '#postal-code, #ccn, #security-code, #phone-number'
    ).keypress(function (e) {
      const keyCode = e.which;
      /*
       8 - (backspace)
       32 - (space)
       48-57 - (0-9)Numbers
     */

      if ((keyCode != 8 || keyCode == 32) && (keyCode < 48 || keyCode > 57)) {
        return false;
      }
    });

    const cleave = new Cleave("#ccn", {
      creditCard: true,
      onCreditCardTypeChanged: function (type) {
        $("#ccn-type").text("Type: " + type);
      },
    });
  }

  if (document.location.pathname.indexOf("/store/") == 0) {
    var prevRatingInput = null;

    $(".btn-rating").click(function () {
      const input = $(this).prev();

      if (prevRatingInput == null) {
        $(".btn-rating-active").removeClass("btn-rating-active");
      } else {
        $(prevRatingInput).prop("checked", false);
        $(prevRatingInput).next().removeClass("btn-rating-active");
      }

      $(this).addClass("btn-rating-active");

      prevRatingInput = input;
    });

    $('input[id*="input-price-"]').keypress(function (e) {
      const keyCode = e.which;
      /*
       8 - (backspace)
       32 - (space)
       48-57 - (0-9)Numbers
     */

      if ((keyCode != 8 || keyCode == 32) && (keyCode < 48 || keyCode > 57)) {
        return false;
      }

      else if (this.value.length > 11) {
        return false;
      }
    });

    $(".clear-filters").click(function () {
      const inputs = $(this).parent().parent();

      inputs.find("input[type=checkbox]").prop("checked", false);
      inputs.find("input[type=checkbox]").removeAttr("checked");

      inputs.find("input[type=number]").removeAttr("value");

      if (prevRatingInput == null) {
        $(".btn-rating-active").removeClass("btn-rating-active");
      } else {
        $(prevRatingInput).prop("checked", false);
        $(prevRatingInput).removeAttr("checked");
        $(prevRatingInput).next().removeClass("btn-rating-active");
      }

      var pageNumber = 0;
      $(".pagination-button").each(function (index) {
        pageNumber = $(this).text();

        $(this)
          .parent()
          .prop("href", "/store/" + pageNumber);
      });

      $(".pagination-button-arrow").each(function (index) {
        pageNumber = $(this).prev().val();

        $(this)
          .parent()
          .prop("href", "/store/" + pageNumber);
      });
    });
  }

  if (document.location.pathname.indexOf("/product") == 0) {

    const statusText = $(".product-status").text();
    $(".product-status").text(statusText.replace(/([A-Z])/g, " $1").trim());

    $('#product-quantity').keypress(function (e) {
      const keyCode = e.which;
      /*
       8 - (backspace)
       32 - (space)
       48-57 - (0-9)Numbers
     */

      if ((keyCode != 8 || keyCode == 32) && (keyCode < 48 || keyCode > 57)) {
        return false;
      }

      else if (this.value.length === 2) {
        return false;
      }
    });

    $(".comment-time").each(function () {
      const commentTime = new Date($(this).text());

      $(this).text(moment(commentTime).fromNow());
    });

    $(".comment-time").show();
  }

  if (document.location.pathname.indexOf("/crud") == 0) {
    $(".btn-edit, .btn-delete, .btn-add, .btn-product-details").click(
      function () {
        const form = $(this).prev();

        form.css("display", "grid");
        form.find(".exit-modal").css("margin-left", "auto");
      }
    );

    $('input[type=number]').keypress(function (e) {
      const keyCode = e.which;
      /*
       8 - (backspace)
       32 - (space)
       48-57 - (0-9)Numbers
     */

      if ((keyCode != 8 || keyCode == 32) && (keyCode < 48 || keyCode > 57)) {
        return false;
      }

      else if (this.value.length > 11) {
        return false;
      }
    });

    $(".edit-tags").each(function () {
      const tags = $(this).nextAll().text();

      $(this).val(tags);
    });

    $(".exit-modal-add-product").click(function () {
      const form = $(this).parent().parent().parent().parent();

      form.css("display", "none");
    });

    $(".exit-modal").click(function () {
      const form = $(this).parent().parent().parent().parent();

      form.css("display", "none");
    });
  }

  if (document.location.pathname.indexOf("/profile") == 0) {
    $(".btn-product-details").click(function () {
      const form = $(this).prev();

      form.css("display", "grid");
      form.find(".exit-modal").css("margin-left", "auto");
    });

    $(".exit-modal").click(function () {
      const form = $(this).parent().parent().parent().parent();

      form.css("display", "none");
    });
  }

  if (document.location.pathname.indexOf("/cart/home") == 0) {
    $("#use-voucher").change(function () {
      $("#form-voucher").submit();
    });
  }
});
