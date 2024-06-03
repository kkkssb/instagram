//Ajax 통신
//모든 게시글 data에 담기
const getBoards = (callback) => {
	$.getJSON(
		"/board/getBoards",
		function(data){
			callback(data);
		}
	)
};
//게시글 지우기
const removeBoard = (loginUser,boardnum, callback) => {
               	$.ajax({
               		type:"POST",
               		url:"/board/removeBoard/"+loginUser+"/"+boardnum,
               		success:function(result){
               			callback(result);
               		}
               	});
               };
//모든 게시글 이미지 result에 담기
const getBoardImgs = (callback) => {
//console.log("img");
	$.getJSON(
		"/board/getBoardImgs",
		function(result){
			callback(result);
		}
	)
};
//해당 게시글 댓글 불러오기
const getReply = (boardnum, callback) => {
console.log("getReply")
	$.getJSON(
		"/board/getReply/"+boardnum,
		function(data){
			callback(data);
		}
	)
};
//모든 likelist data에 담기
const getLikeList = (callback) => {
	$.getJSON(
		"/board/likeList",
		function(data){
			callback(data);
		}
	)
};
//유저 팔로워 담기
const getFollowlist = (user, callback) => {
	$.getJSON(
		"/board/followList/"+user,
		function(data){
			callback(data);
		}
	)
};
//댓글 등록하기
const registReply = (loginUser,boardnum,text, callback) => {
               	$.ajax({
               		type:"POST",
               		url:"/board/registReply/"+loginUser+"/"+boardnum,
               		contentType: "application/json",
                    data: JSON.stringify({ text: text }),
               		success:function(result){
               			callback(result);
               		}
               	});
               };
//댓글 지우기
const removeReply = (loginUser,replynum,boardnum, callback) => {
               	$.ajax({
               		type:"POST",
               		url:"/board/removeReply/"+loginUser+"/"+replynum+"/"+boardnum,
               		success:function(result){
               			callback(result);
               		}
               	});
               };
//게시글 좋아요 누르기
const clickLike = (loginUser,boardnum, callback) => {
               	$.ajax({
               		type:"POST",
               		url:"/board/clickLike/"+loginUser+"/"+boardnum,
               		success:function(result){
               			callback(result);
               		}
               	});
               };
//게시글 좋아요 취소
const cancelLike = (loginUser,boardnum, callback) => {
               	$.ajax({
               		type:"POST",
               		url:"/board/cancelLike/"+loginUser+"/"+boardnum,
               		success:function(result){
               			callback(result);
               		}
               	});
               };
//게시글 쓴 유저 팔로우하기
const Follow = (loginUser,writer, callback) => {
               	$.ajax({
               		type:"POST",
               		url:"/board/follow/"+loginUser+"/"+writer,
               		success:function(result){
               			callback(result);
               		}
               	});
               };
//팔로우 취소
const cancelFollow = (loginUser,writer, callback) => {
               	$.ajax({
               		type:"POST",
               		url:"/board/cancelFollow/"+loginUser+"/"+writer,
               		success:function(result){
               			callback(result);
               		}
               	});
               };
//댓글폼 복제하기
const replyBox = (toCopy, num, appendTo) => {
	let reply = toCopy.cloneNode(true);
	let trTagClass = "reply_box"+num+" ";
	reply.removeAttribute('id');
	reply.className = trTagClass;

	appendTo.appendChild(reply);
    events();
	return reply;
};
//새로고침 전 팔로우버튼 표시변경
//[ex)어떤 유저 팔로우 클릭 시 해당 유저 게시글의 팔로우 버튼들 모두 팔로잉으로 변경]
const followBtn = (writer, ifClick) => {
	document.querySelectorAll(".follow." +writer)
		.forEach(followTag => {
			followTag.style.display = ifClick?"none":"block";
		});
	document.querySelectorAll(".follow1." +writer)
		.forEach(follow1Tag => {
			follow1Tag.style.display = ifClick?"block":"none";
		});
};


//페이지 로드시 getBoards ajax실행
window.onload = () => {
	getBoards(data=>{
	copyStoryForm(data);
	});
};


//게시글스토리 작성
const board = document.querySelector(".s_title");
const loginUser = document.querySelector("#writeBtn").dataset.hiddenValue;
const copyStoryForm = boards => {
	let boardForms = [];
	for(let i=0;i<boards.length;i++){
		const boardForm =
		document.querySelector("#boardForm").cloneNode(true); //코드 복제
		boardForm.classList.add("board"+boards[i].boardnum);//복제한 코드에 boardnum클래스 붙이기
		boardForm.removeAttribute('id'); //기존에 존재했던 id삭제
		boardForms[i] = boardForm; //복제한폼을 배열에 담기
		board.appendChild(boardForm); //게시글이 화면에 띄워져야 할 부분에 붙이기

		//코드를 간단하게 하기위해 필요한 변수 선언해 두기(board관련 변수)
        let boardnum = boards[i].boardnum;
        let contents = boards[i].contents;
        let nickName = boards[i].nickName;
        let likes = boards[i].likes;
        let modify = boards[i].modify;

		let findTag = ".board"+boards[i].boardnum+" "; //내가 사용하는 복제폼 안에서 클래스 찾기위해 findtag사용

		//클래스 찾는 코드 간결하게 하기위해 선언(복제폼 안에 들어있는 클래스들)
		let profil = document.querySelector(findTag+".profil");
		let top_img = document.querySelector(findTag+".top_img");
		let top_a = document.querySelector(findTag+".top_a");
		let scontents1 = document.querySelector(findTag+".scontents1");
		let likecnt = document.querySelector(findTag+".likecnt");
        let uids = document.querySelector(findTag+".uids");
        let slider_img = document.querySelector(findTag+".slider_img");
        let slider = document.querySelector(findTag+".slider");
        let slider__inner = document.querySelector(findTag+".slider__inner");
		let btn = document.querySelector(findTag+".slider__btn");
		let btn_a = document.querySelector(findTag+".slider__btn a");
		let mthrt = document.querySelector(findTag+".like");
		let fullhrt = document.querySelector(findTag+".like1");
		let follow = document.querySelector(findTag+".follow");
		let following = document.querySelector(findTag+".follow1");
		let reply = document.querySelector(findTag+".reply");
		let reply_box = document.querySelector(findTag+".reply_box");
		let seeAll = document.querySelector(findTag+".seeAll");
		let reply_uid = document.querySelector(findTag+".uid1");
		let reply_txt = document.querySelector(findTag+".uid2");
		let replycnt1 = document.querySelector(findTag+".replycnt1");
		let dot_ = document.querySelector(findTag+".dot_");
		let modifyB = document.querySelector(findTag+".modifyB");
		let removeB = document.querySelector(findTag+".removeB");
        let replyForm = document.querySelector("#replyForm");

		//복제 폼 안에 데이터넣기
		top_img.setAttribute("src", "/user/getproImg?user="+nickName); //프로필 사진
		top_a.innerHTML = nickName; //프로필 사진 옆 닉네임
		likecnt.innerHTML = "좋아요 " + likes + "개"; //게시글 좋아요 개수
		uids.innerHTML = nickName;// 게시글 내용 왼쪽 닉네임
		scontents1.innerHTML = contents; //게시글 내용
		seeAll.innerHTML="댓글 0개 모두보기";
		follow.classList.add(nickName);//팔로우표시 div클래스에 닉네임추가
		following.classList.add(nickName);

        //닉네임과 로그인유저가 같을 시 팔로우 버튼 숨기기, 글 수정,삭제 버튼 보이기
        if(nickName === loginUser){
            follow.style.display = "none";
            following.style.display = "none";
            dot_.style.display = "block";
        }

        //게시글 지우기
        removeB.addEventListener("click", () => {
            removeBoard(loginUser,boardnum, result => {
            let re = document.querySelector(findTag);
            re.remove();
            });
        });


        //댓글 불러오기
        getReply(boardnum, data => {
        	for(let i=0; i<data.length; i++){
                let findTag1 = ".reply_box"+data[i].replynum+" ";
        	    let copyReply = replyBox(replyForm, data[i].replynum, reply_box);//reply_box를 복제
                let re = document.querySelector(findTag1);

                copyReply.children[0].innerText = data[i].nickName;//복제폼에 데이터 넣기
                copyReply.children[1].innerText = data[i].contents;
                seeAll.innerHTML="댓글"+data.length+"개 모두보기";

                let remove = document.querySelector(findTag1+".removeR");
                let modify = document.querySelector(findTag1+".modifyR");

                remove.addEventListener("click", () => {
                console.log("댓글삭제")
                //  e.preventDefault();
                    //댓글 지우는 함수 호출
                    removeReply(loginUser, data[i].replynum,boardnum, result => {
                        re.remove();
                        data.length--
                        seeAll.innerHTML="댓글"+data.length+"개 모두보기"; //게시글 좋아요 개수
                    });
                });
        	}

		    //댓글 달기
		    reply.addEventListener("keydown", e => {
                if(e.keyCode === 13){ //엔터 누를 시
				    e.preventDefault();
				    data.length++;//댓글 갯수 증가
				    const text = reply.value;//댓글 내용
				    //댓글 등록 함수
				    registReply(loginUser, boardnum, text, result => {
				        seeAll.innerHTML = "댓글" +data.length+ "개 모두보기";
				        let copyReply = replyBox(replyForm, boardnum, reply_box);
                        copyReply.children[0].innerText = loginUser;
                        copyReply.children[1].innerText = text;
                    });
                    reply.value="";
			    }
		    });
        });



		//하트 표시
		getLikeList(data => {
		    for(let i=0; i<data.length; i++){
		        if(boardnum == data[i].boardnum && loginUser === data[i].nickName){
		                console.log("하트"+data[i].nickName)
		               mthrt.style.display = "none";
                       fullhrt.style.display = "block";
                }
		    }
		});
		//좋아요 누름 클릭이벤트
		mthrt.addEventListener("click", () => {
		    clickLike(loginUser, boardnum, result => {
		    likes++;
		    likecnt.innerHTML = "좋아요 " + likes + "개"; //게시글 좋아요 개수
		    });
		});
		//좋아요 취소 클릭이벤트
		fullhrt.addEventListener("click", () => {
		    cancelLike(loginUser, boardnum, result => {
            likes--;
        	likecnt.innerHTML = "좋아요 " + likes + "개"; //게시글 좋아요 개수
        	});
        });


        //팔로우 표시
        getFollowlist(loginUser, data => {
        	for(let i=0; i<data.length; i++){
        		if(nickName == data[i].writer && loginUser === data[i].user){
        		    follow.style.display = "none";
                    following.style.display = "block";
                }
        	}
        });
        //팔로우 클릭이벤트
        let writer=boards[i].nickName;
        follow.addEventListener("click", () => {
            Follow(loginUser, writer, result => {
               let check = document.querySelectorAll("."+writer);
               if(check){
               	    followBtn(writer, true);
               }

            });
        });
        //팔로우 취소 클릭이벤트
        following.addEventListener("click", () => {
            cancelFollow(loginUser, writer, result => {
                 let check = document.querySelectorAll("."+writer);
                 if(check){
                    followBtn(writer, false);
                 }
            });
        });

		let numOfPics = 0; //이미지 갯수 초기화
		//이미지 불러오기
		getBoardImgs(result => {
		    for(let j=0;j<result.length;j++){
		    //이미지의 boardnum과 복제폼 boardnum이 일치하면 이미지 넣기
		        if(boardnum == result[j].boardnum) {
		            let systemname = result[j].systemname;
		            let slider = document.createElement("div");
                    slider.className = "slider";
		            let img = document.createElement("img");
		            img.className="slider_img";
                    img.src = '/board/getBImgs?systemname='+systemname;
                    slider.appendChild(img);
                    slider__inner.appendChild(slider);
                    numOfPics++;
                    btn.children[0].classList.add("prev"+boardnum); //슬라이드버튼 누를시 생기는 오류를 방지하기위해
                    btn.children[1].classList.add("prev"+boardnum); //복제폼 슬라이드 버튼에 클래스 붙이기
		        }
            }
		    if(numOfPics<2){ //이미지가 2개 이상일 때만 슬라이드 버튼 생성
		        btn.style.display="none";
		    }
		    //이미지 슬라이드 시 이벤트
		    const sliderWrap = document.querySelector(findTag+".slider__wrap");
            const sliderImg = document.querySelector(findTag+".slider__img");
		    let currentIndex = 0;                       // 현재 이미지
            let sliderCount = numOfPics;            // 이미지 갯수
            let sliderWidth = 500;  // 이미지 가로값

		    function gotoSlider(num){
                slider__inner.style.transition = "all 400ms";
                slider__inner.style.transform = "translateX("+ -sliderWidth * num +"px)";
                currentIndex = num;
            }

            // 버튼 클릭했을 때
            document.querySelectorAll(".prev"+boardnum).forEach((btn, index) => {
                btn.addEventListener("click", () => {
                    let prevIndex = (currentIndex + (sliderCount -1)) % sliderCount;
                    let nextIndex = (currentIndex + 1) % sliderCount;

                    if(btn.classList.contains("prev")){
                        gotoSlider(prevIndex);
                    } else {
                        gotoSlider(nextIndex);
                    }
                });
            });
		});


		for(let i=0;i<boardForms.length;i++){
        		boardForms[i].style.display = "block";
        }
        events();
	}
	return boardForms;
};
