

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
//모든 게시글 이미지 data에 담기
const getBoardImgs = (callback) => {
console.log("img");
	$.getJSON(
		"/board/getBoardImgs",
		function(result){
			callback(result);
		}
	)
};

window.onload = () => {
	getBoards(data=>{
	copyStoryForm(data);
	});

};

const board = document.querySelector(".s_title");
const copyStoryForm = boards => {
	let boardForms = [];
	for(let i=0;i<boards.length;i++){
		const boardForm =
		document.querySelector("#boardForm").cloneNode(true);
        console.log(boardForm);
		boardForm.classList.add("board"+boards[i].boardnum);
		boardForm.removeAttribute('id');
		boardForms[i] = boardForm;
		board.appendChild(boardForm);

        let boardnum = boards[i].boardnum;
        let contents = boards[i].contents;
        let nickName = boards[i].nickName;
        let likes = boards[i].likes;
        let modify = boards[i].modify;

		let findTag = ".board"+boards[i].boardnum+" ";

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



		//데이터 하나씩 넣기
		top_img.setAttribute("src", "/user/getproImg?user="+nickName);
		console.log(nickName)
		top_a.innerHTML = nickName;
		likecnt.innerHTML = "좋아요 " + likes + "개";
		uids.innerHTML = nickName;
		scontents1.innerHTML = contents;

		let numOfPics = 0;
		getBoardImgs(result => {
		    for(let j=0;j<result.length;j++){
		        if(boardnum == result[j].boardnum) {
		            let systemname = result[j].systemname;
		            console.log(systemname)
		            let slider = document.createElement("div");
                    slider.className = "slider";
		            let img = document.createElement("img");
		            img.className="slider_img";
                    img.src = '/board/getBImgs?systemname='+systemname;
                    slider.appendChild(img);
                    slider__inner.appendChild(slider);
                    numOfPics++;
                    btn.children[0].classList.add("prev"+boardnum);
                    btn.children[1].classList.add("prev"+boardnum);
		        }
            }
		    console.log(numOfPics)
		    if(numOfPics<2){
		    btn.style.display="none";
		    }
		//sider click event
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

	return boardForms; //나중에 display none 전부 해제
};
