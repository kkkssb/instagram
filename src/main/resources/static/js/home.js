 const events = () => {

    $(".like").click(function(){
    console.log("ada")
 	    $(this).next().show();
 	    $(this).hide();
 	})
 	$(".like1").click(function(){
 	    $(this).prev().show();
 	    $(this).hide();
 	})
	//글 수정삭제
	$(".dot").click(function(){
	    $(this).next().show();
	    $(this).hide();
	    $(this).next().css('display', 'flex');
	})
	$(".dot_sub").click(function(){
	    $(this).prev().show();
	    $(this).hide();
	})

	//팔로우 팔로잉
	$(".follow").click(function(){
	    $(this).next().show();
	    $(this).hide();
	})
	$(".follow1").click(function(){
	    $(this).prev().show();
	    $(this).hide();
	})

	//글내용 더보기,간략히
	 $('.scontents').each(function(){
    var content = $(this).children('.scontents1');
    var content_txt = content.text();
        if(content_txt.length >= 150){
        console.log(content_txt.length);
        console.log("afda");
        $(this).find(".more").show();
        }
        else{
            $(this).find(".more").hide();
            $(this).find(".less").hide();
        }

        $(this).find(".more").click(function(){
             $(this).prevUntil("scontents1").show();
            $(this).hide();
            content.css('overflow', 'visible');
            content.css('display', 'block');
            content.css("white-space","normal");
        });

        $(this).find(".less").click(function(){
            $(this).prev().hide();
            $(this).next(".more").show();
            $(this).hide();
            content.css("overflow","hidden");
            content.css("height","auto");
            content.css("white-space","nowrap");
        });
    });


	//댓글더보기,간략히
	$(".seeAll").click(function(){
	    $(this).prevUntil("replycnt").show();
	    $(this).hide();
	})
	$(".less1").click(function(){
	    $(this).prevUntil("replycnt").hide();
	    $(this).next().show();
	    $(this).hide();
	})

	//좋아요하트


	//메시지아이콘 클릭시 댓글모두보기
	$(".msg").click(function(){
	    $(".replycnt1").show();
	    $(".seeAll").hide();
	    $(".less1").show();
	})

	//댓글 수정삭제버튼
		$(".uid3").click(function(){
	    $(this).next().show();
	    $(this).hide();
	})
		$(".uid4").click(function(){
	    $(this).prev().show();
	    $(this).hide();
	})

	//좋아요 리스트
		$(".deleteimg").click(function(){
	    $(".likebox").hide();
	    $('body').css({ overflow: 'visible' });
	    $('#cover').css({ display: 'none' });

	})


};

