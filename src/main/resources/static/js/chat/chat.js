const socket = new SockJS('/ws-stomp');
const stompClient = Stomp.over(socket);

const chatList = document.querySelector('.chatting-list');
const chatInput = document.querySelector('.chatting-input');
const sendButton = document.querySelector('.send-button');
const displayContainer = document.querySelector('.display-container');
const fileInput = document.getElementById('file');
const roomId = document.querySelector('.room-id');
const productId = document.querySelector('.product-id');
const userEmail = document.querySelector('.email');
const userId = document.querySelector('.sender-id');
const senderNickname = document.querySelector('.nickname');

stompClient.connect({
  senderId : userId.value,
  roomId: roomId.value,
  productId: productId.value
}, function (frame) {
  console.log(`Connected:  ${frame}`);
  getMessageEnter();
  getMessage();
  sendEnterMessage();



  stompClient.subscribe(`/room/connect/${roomId.value}`, data => {
    const {type, userEmail} = JSON.parse(data.body)
    console.log(`커넥트 결과: ${type}`)
    if (type === 'CONNECT') {
      $.ajax({
        url: '/chat-room/room',
        contentType: 'application/json',
        type: 'post',
        data: JSON.stringify({
          roomId: roomId.value,
          productId: productId.value
        }),
        success: data => {
          console.log(`채팅방 ajax 성공`)
          const readCount = document.querySelectorAll('.read-count');
          readCount.forEach((count) => {
            count.textContent = ""
          });

        },
        error: data => {
          console.log("실패", data)
        }
      })
    }})

  stompClient.send(`/send/connect/${roomId.value}`, {},
      JSON.stringify({userEmail: userEmail.value}))
});

chatInput.addEventListener('keypress', (event) => {
  if (event.keyCode === 13) {
    sendMessage().then();
  }
})

function sendEnterMessage(){
  stompClient.send(`/send/enter/${roomId.value}`,{},
      JSON.stringify({
        roomId:roomId.value,
        email:userEmail.value,
        senderId:userId.value,
        nickName:senderNickname.value
      }))
}

async function sendMessage() {
  const file = fileInput.files[0];
  if(file){
    const formDate = new FormData();
    formDate.append('file', file);
    const response = await fetch('/upload',{
      method:'POST',
      body:formDate
    })
    if(response.ok){
      const imageUrl = await response.text();
      const param={
        chatRoomId: roomId.value,
        email: userEmail.value,
        senderId: userId.value,
        nickname: senderNickname.value,
        type: 'IMAGE',
        message:imageUrl
      }
      if(fileInput.value!==''){
        stompClient.send(`/send/message/${roomId.value}`,{},JSON.stringify(param));
      }
      fileInput.value = "";
    }
  }
  if(chatInput.value!==''){
    const param = {
      chatRoomId: roomId.value,
      email: userEmail.value,
      senderId: userId.value,
      nickname: senderNickname.value,
      type: 'TEXT',
      message: chatInput.value
    }
    stompClient.send(`/send/message/${roomId.value}`, {}, JSON.stringify(param))
    chatInput.value = ''
  }
}

sendButton.addEventListener('click', sendMessage);

function getMessageEnter() {

  stompClient.subscribe('/user/room/'+roomId.value, (data) => {
    console.log(data);
    const {nickName, senderId,message,type,readCount ,sendTime} = JSON.parse(data.body);
    const item = new LiModel(nickName, senderId,message,type,readCount,sendTime )
    if(type==='IMAGE'){
      item.makeLiImg()
    }else {
      item.makeLi();
    }
    displayContainer.scrollTo(0, displayContainer.scrollHeight);
  });
}
function getMessage() {

  stompClient.subscribe(`/room/${roomId.value}`, (data) => {
    console.log(data);
    const {nickName, senderId,message,type,readCount ,sendTime} = JSON.parse(data.body);
    const item = new LiModel(nickName, senderId,message,type,readCount,sendTime )
    if(type==='IMAGE'){
      item.makeLiImg()
    }else {
      item.makeLi();
    }
    displayContainer.scrollTo(0, displayContainer.scrollHeight);
  });
}

function LiModel(name, senderId ,msg, type,readCount,time) {
  this.name = name;
  this.senderId = senderId
  this.msg = msg;
  this.type = type;
  this.readCount = readCount;
  this.time = time;



  if(readCount === null || readCount === 0){
    this.readCount='';
  }

  this.makeLi = () => {
    const li = document.createElement('li');
    console.log(this.senderId)
    console.log(userId.value)
    li.classList.add(userId.value === this.senderId.toString() ? 'sent' : 'received')
    const dom = ` <span class="profile">
          <span class="user">${this.name}</span>
          <img class="image"
            src=""
            alt="any">
        </span>
        <span class="message">${this.msg}</span>
        <ul>
        <li class="read-count">${this.readCount}</li>
        <li class="time">${this.time}</li>
</ul>
`
    li.innerHTML = dom;
    chatList.appendChild(li);

  }
  this.makeLiImg = () => {
    const li = document.createElement('li');
    li.classList.add(userId.value === this.senderId.toString() ? 'sent' : 'received')
    const dom = ` <span class="profile">
          <span class="user">${this.name}</span>
          <img class="image"
          src=""
            alt="any">
        </span>
        <span class="message"><img src="${this.msg}" alt="text-img" class="message-img"></span>
        <ul>
        <li class="read-count">${this.readCount}</li>
        <li class="time">${this.time}</li>
</ul>`;
    li.innerHTML = dom;
    chatList.appendChild(li);

  }



}