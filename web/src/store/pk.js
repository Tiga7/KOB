export default {
	state: {
		status: "matching", //matching 正在匹配  playing 正在对战
		// status: "playing", //matching 正在匹配  playing 正在对战
		socket: null,
		opponent_username: "", //对手名字
		opponent_photo: "", //对手照片
		gamemap: null,
	},
	getters: {},
	mutations: {
		updateSocket(state, socket) {
			state.socket = socket;
		},
		updateOpponent(state, opponent) {
			state.opponent_username = opponent.username;
			state.opponent_photo = opponent.photo;
		},
		updateStatus(state, status) {
			state.status = status;
		},
		updateGamemap(state, gamemap) {
			state.gamemap = gamemap;
		},
	},
	actions: {},
	modules: {},
};
