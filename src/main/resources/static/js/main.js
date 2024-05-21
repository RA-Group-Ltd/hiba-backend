'use strict';

const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');
const logout = document.querySelector('#logout');

let stompClient = null;
let phone = null;
let fullname = null;
let token = null;
let selectedUserId = null;
let selectedChatId = null;
let currentUserId = null;
let chatId = null; // добавьте это

function parseJwt(token) {
    if (!token) {
        console.error('Token is null or undefined');
        return null;
    }
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}

async function getUserIdByPhone(phone) {
    try {
        console.log(`Fetching user ID for phone: ${phone}`);
        const response = await fetch(`/auth/userId?phone=${phone}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        console.log(`Received user ID: ${data.userId}`);
        return data.userId;
    } catch (error) {
        console.error('Failed to fetch user ID:', error);
    }
}

async function connect(event) {
    event.preventDefault(); // Предотвращаем перезагрузку страницы

    phone = document.querySelector('#nickname').value.trim();
    fullname = document.querySelector('#fullname').value.trim();
    token = document.querySelector('#token').value.trim();

    console.log('Connecting with:', { phone, fullname, token });

    if (phone && fullname && token) {
        const decodedToken = parseJwt(token);
        if (!decodedToken) {
            console.error('Failed to decode token');
            return;
        }
        console.log('Decoded token:', decodedToken);
        const userPhone = decodedToken.sub; // Используем поле `sub` для userId

        if (!userPhone) {
            console.error('userPhone is undefined. Please check your token structure.');
            return;
        }

        currentUserId = await getUserIdByPhone(userPhone);
        if (!currentUserId) {
            console.error('currentUserId is undefined. Failed to retrieve user ID.');
            return;
        }

        console.log(`Logged in as user ID: ${currentUserId}`);

        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({ Authorization: 'Bearer ' + token }, onConnected, onError);
    } else {
        console.error('Phone, fullname, or token is missing');
    }
}

function onConnected() {
    console.log('Connected to WebSocket server');

    if (currentUserId) {
        stompClient.subscribe(`/user/${currentUserId}/queue/messages`, onMessageReceived);
        console.log(`Subscribed to /user/${currentUserId}/queue/messages`);
    } else {
        console.error('currentUserId is undefined. Cannot subscribe to user queue messages.');
    }

    stompClient.subscribe(`/user/public`, onMessageReceived);
    console.log('Subscribed to /user/public');

    // Register the connected user
    stompClient.send("/app/user.addUser",
        {},
        JSON.stringify({ phone: phone, fullName: fullname })
    );
    document.querySelector('#connected-user-fullname').textContent = fullname;
    // findAndDisplayConnectedUsers().then();
    fetchUserChats().then();
}

async function fetchUserChats() {
    try{
        console.log('fetching user chats')
        const chatsResonse = await fetch(`chats/client/${currentUserId}`)
        if (!chatsResonse.ok){
            throw new Error(`HTTP error! status: ${chatsResonse.status}`);
        }
        let chatsRes = await chatsResonse.json()

        if (!Array.isArray(chatsRes)){
            throw new TypeError('Expected an array of chats');
        }

        // chatsRes = chatsRes.filter(chat => chat.client_id === currentUserId);
        console.log('chats:', chatsRes)

        const chatList = document.getElementById('chatsList');
        chatList.innerHTML = '';

        chatsRes.forEach(chat => {
            appendChatListElement(chat, chatList);
            if (chatsRes.indexOf(chat) < chatsRes.length - 1) {
                const separator = document.createElement('li');
                separator.classList.add('separator');
                chatList.appendChild(separator);
            }
        });

    }catch ( error){
        console.error('Failed to fetch user chats:', error)
    }
}

async function findAndDisplayConnectedUsers() {
    try {
        console.log('Fetching connected users');
        const connectedUsersResponse = await fetch('/auth/users');
        if (!connectedUsersResponse.ok) {
            throw new Error(`HTTP error! status: ${connectedUsersResponse.status}`);
        }
        let connectedUsers = await connectedUsersResponse.json();
        console.log('Connected users:', connectedUsers);

        if (!Array.isArray(connectedUsers)) {
            throw new TypeError('Expected an array of users');
        }

        connectedUsers = connectedUsers.filter(user => user.id !== currentUserId);
        const connectedUsersList = document.getElementById('connectedUsers');
        connectedUsersList.innerHTML = '';

        connectedUsers.forEach(user => {
            appendUserElement(user, connectedUsersList);
            if (connectedUsers.indexOf(user) < connectedUsers.length - 1) {
                const separator = document.createElement('li');
                separator.classList.add('separator');
                connectedUsersList.appendChild(separator);
            }
        });
    } catch (error) {
        console.error('Failed to fetch connected users:', error);
    }
}

function appendChatListElement(chat, chatsList) {
    const listItem = document.createElement('li');
    listItem.classList.add('chat-item');
    listItem.id = chat.id;

    // const userImage = document.createElement('img');
    // userImage.src = '../img/user_icon.png';
    // userImage.alt = user.fullName;

    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = chat.clientId;

    // const receivedMsgs = document.createElement('span');
    // receivedMsgs.textContent = '0';
    // receivedMsgs.classList.add('nbr-msg', 'hidden');

    // listItem.appendChild(userImage);
    listItem.appendChild(usernameSpan);
    // listItem.appendChild(receivedMsgs);
    console.log(listItem)

    listItem.addEventListener('click', chatItemClick);

    chatsList.appendChild(listItem);
}


function appendUserElement(user, connectedUsersList) {
    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    listItem.id = user.id;

    const userImage = document.createElement('img');
    userImage.src = '../img/user_icon.png';
    userImage.alt = user.fullName;

    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = user.fullName;

    const receivedMsgs = document.createElement('span');
    receivedMsgs.textContent = '0';
    receivedMsgs.classList.add('nbr-msg', 'hidden');

    listItem.appendChild(userImage);
    listItem.appendChild(usernameSpan);
    listItem.appendChild(receivedMsgs);

    listItem.addEventListener('click', userItemClick);

    connectedUsersList.appendChild(listItem);
}

function chatItemClick(event) {
    document.querySelectorAll('.chat-item').forEach(item => {
        item.classList.remove('active');
    });
    messageForm.classList.remove('hidden');

    const clickedChat = event.currentTarget;
    clickedChat.classList.add('active');

    selectedChatId = clickedChat.getAttribute('id');

    if (selectedChatId) {
        console.log('Selected chat ID:', selectedChatId);
        fetchAndDisplayUserChat().then();
    } else {
        console.error('selectedChatId is undefined. Cannot fetch and display user chat.');
    }

    const nbrMsg = clickedChat.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = '0';
}


function userItemClick(event) {
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });
    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectedUserId = clickedUser.getAttribute('id');

    if (selectedUserId) {
        console.log('Selected user ID:', selectedUserId);
        fetchAndDisplayUserChat().then();
    } else {
        console.error('selectedUserId is undefined. Cannot fetch and display user chat.');
    }

    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = '0';
}

function displayMessage(senderId, content) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');
    if (senderId === currentUserId) {
        messageContainer.classList.add('sender');
    } else {
        messageContainer.classList.add('receiver');
    }
    const message = document.createElement('p');
    message.textContent = content;
    messageContainer.appendChild(message);
    chatArea.appendChild(messageContainer);
}


async function fetchAndDisplayUserChat() {
    try {
        console.log("fetch chat messages")
        if (!selectedChatId) {
            throw new Error('No chat selected');
        }
        const userChatResponse = await fetch(`/chats/by-id/${selectedChatId}`);
        if (!userChatResponse.ok) {
            throw new Error(`HTTP error! status: ${userChatResponse.status}`);
        }
        console.log("json")
        if(userChatResponse == null){
            console.log("No messages")
            return
        }
        const userChat = await userChatResponse.json();
        console.log('User chat:', userChat.id);

        // if (!Array.isArray(userChat)) {
        //     throw new TypeError('Expected an array of messages');
        // }

        chatArea.innerHTML = '';
        userChat.messages.forEach(mes => {
            if(mes.senderType == 'CLIENT'){
                displayMessage(userChat.chat.clientId, mes.content);
            }else {
                displayMessage(userChat.chat.supportId, mes.content);
            }
        });
        chatArea.scrollTop = chatArea.scrollHeight;

        // Устанавливаем chatId для текущего чата
        chatId = userChat.length > 0 ? userChat[0].chat.id : null;
    } catch (error) {
        console.error('Failed to fetch user chat:', error);
    }
}

function onError(error) {
    console.error('Could not connect to WebSocket server:', error);
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function sendMessage(event) {
    const messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        const chatMessage = {
            senderType: "CLIENT",
            content: messageContent,
            timestamp: new Date().toISOString(),
            chat: selectedChatId // добавьте chatId сюда
        };
        console.log(chatMessage)
        stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
        displayMessage(currentUserId, messageInput.value.trim());
        messageInput.value = '';
    }
    chatArea.scrollTop = chatArea.scrollHeight;
    event.preventDefault();
}

async function onMessageReceived(payload) {
    console.log('Message received:', payload);
    await findAndDisplayConnectedUsers();
    const message = JSON.parse(payload.body);
    if (selectedUserId && selectedUserId === message.senderId) {
        displayMessage(message.senderId, message.content);
        chatArea.scrollTop = chatArea.scrollHeight;
    }

    if (selectedUserId) {
        document.querySelector(`#${selectedUserId}`).classList.add('active');
    } else {
        messageForm.classList.add('hidden');
    }

    const notifiedUser = document.querySelector(`#${message.senderId}`);
    if (notifiedUser && !notifiedUser.classList.contains('active')) {
        const nbrMsg = notifiedUser.querySelector('.nbr-msg');
        nbrMsg.classList.remove('hidden');
        nbrMsg.textContent = (parseInt(nbrMsg.textContent) || 0) + 1;
    }
}

function onLogout() {
    if (stompClient) {
        stompClient.send("/app/user.disconnectUser",
            {},
            JSON.stringify({ id: currentUserId, fullName: fullname, status: 'OFFLINE' })
        );
    }
    window.location.reload();
}

usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);
logout.addEventListener('click', onLogout, true);
window.onbeforeunload = () => onLogout();
