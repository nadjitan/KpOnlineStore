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
      'input[id*="input-price-"], #postal-code, #ccn, #security-code, #phone-number'
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

      if (prevRatingInput != null) {
        $(prevRatingInput).prop("checked", false);
        $(prevRatingInput).next().css("background-color", "transparent");
      }

      $(this).css("background-color", "aquamarine");

      prevRatingInput = input;
    });

    $(".clear-filters").click(function () {
      const inputs = $(this).parent();

      inputs.find("input").prop("checked", false);
    });
  }

  if (document.location.pathname.indexOf("/crud") == 0) {
    $(".btn-edit, .btn-delete, .btn-add, .btn-product-details").click(
      function () {
        const form = $(this).prev();

        form.css("display", "grid");
        form.find(".exit-modal").css("margin-left", "auto");
      }
    );

    $(".exit-modal-add-product").click(function () {
      const form = $(this).parent();

      form.css("display", "none");
    });

    $(".exit-modal").click(function () {
      const form = $(this).parent().parent();

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
      const form = $(this).parent().parent();

      form.css("display", "none");
    });
  }
});
