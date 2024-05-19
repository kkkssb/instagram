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
//모든 게시글 이미지 result에 담기
const getBoardImgs = (callback) => {
console.log("img");
	$.getJSON(
		"/board/getBoardImgs",
		function(result){
			callback(result);
		}
	)
};
//모든 likelist data에 담기
const getLikeList = (callback) => {
console.log("getLikelist")
	$.getJSON(
		"/board/likeList",
		function(data){
			callback(data);
		}
	)
};
//유저 팔로워 담기
const getFollowlist = (user, callback) => {
console.log("getFollowlist")
	$.getJSON(
		"/board/followList/"+user,
		function(data){
			callback(data);
		}
	)
};
const clickLike = (loginUser,boardnum, callback) => {
               	$.ajax({
               		type:"POST",
               		url:"/board/clickLike/"+loginUser+"/"+boardnum,
               		success:function(result){
               			callback(result);
               		}
               	});
               };

const cancelLike = (loginUser,boardnum, callback) => {
               	$.ajax({
               		type:"POST",
               		url:"/board/cancelLike/"+loginUser+"/"+boardnum,
               		success:function(result){
               			callback(result);
               		}
               	});
               };
const Follow = (loginUser,writer, callback) => {
               	$.ajax({
               		type:"POST",
               		url:"/board/follow/"+loginUser+"/"+writer,
               		success:function(result){
               			callback(result);
               		}
               	});
               };
const cancelFollow = (loginUser,writer, callback) => {
               	$.ajax({
               		type:"POST",
               		url:"/board/cancelFollow/"+loginUser+"/"+writer,
               		success:function(result){
               			callback(result);
               		}
               	});
               };
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

		//복제 폼 안에 데이터넣기
		top_img.setAttribute("src", "/user/getproImg?user="+nickName); //프로필 사진
		top_a.innerHTML = nickName; //프로필 사진 옆 닉네임
		likecnt.innerHTML = "좋아요 " + likes + "개"; //게시글 좋아요 개수
		uids.innerHTML = nickName;// 게시글 내용 왼쪽 닉네임
		scontents1.innerHTML = contents; //게시글 내용
		follow.classList.add(nickName);
		following.classList.add(nickName);
		//하트 표시
		getLikeList(data => {
		    for(let i=0; i<data.length; i++){
		        if(boardnum==data[i].boardnum && loginUser===data[i].nickName){
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
        if(nickName===loginUser){
        follow.style.display = "none";
        following.style.display = "none";
        }
        getFollowlist(loginUser, data => {
        console.log("daf")
        	for(let i=0; i<data.length; i++){
        		if(nickName==data[i].writer && loginUser===data[i].user){
        		    follow.style.display = "none";
                    following.style.display = "block";
                }
        	}
        });

        let writer=boards[i].nickName;
        //팔로우 클릭이벤트
        follow.addEventListener("click", () => {
            Follow(loginUser, writer, result => {
               let check = document.querySelectorAll("."+writer);
               				if(check){
               					followBtn((writer), true);
               				}

            });
        });
        //팔로우 취소 클릭이벤트
        following.addEventListener("click", () => {
            cancelFollow(loginUser, writer, result => {
                 let check = document.querySelectorAll("."+writer);
                       if(check){
                           followBtn((writer), false);
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
            })
		});


		for(let i=0;i<boardForms.length;i++){
        		boardForms[i].style.display = "block";
        	}
        	events();

	}

	return boardForms;
};
