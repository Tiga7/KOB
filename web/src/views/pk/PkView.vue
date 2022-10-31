<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'" />
    <MatchGround v-if="$store.state.pk.status === 'matching'" />
    <ResultBoard v-if="$store.state.pk.loser != 'none'" />
</template>

<script>
import PlayGround from './PlayGround.vue'
import MatchGround from './MathGrount.vue'
import ResultBoard from './ResultBoard.vue'
import { onMounted, onUnmounted } from 'vue'
import { useStore } from 'vuex'

export default {
    components: {
        PlayGround,
        MatchGround,
        ResultBoard
    },
    setup() {
        const store = useStore();

        const socketUrl = `ws://localhost:3030/websocket/${store.state.user.token}`;

        let socket = null;

        store.commit("updateLoser", "none");

        onMounted(() => {

            store.commit("updateOpponent", {
                username: "我的对手",
                photo: "https://img2.baidu.com/it/u=2602524975,4281341375&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400",
            })
            socket = new WebSocket(socketUrl);


            socket.onopen = () => {
                store.commit("updateSocket", socket);
                console.log("建立连接");
            }

            socket.onmessage = (msg) => {
                const data = JSON.parse(msg.data);
                // console.log(data);
                if (data.event === "start-matching") {
                    store.commit("updateOpponent", {
                        username: data.opponent_username,
                        photo: data.opponent_photo,
                    })
                    setTimeout(() => {
                        store.commit("updateStatus", "playing");
                    }, 2000);

                    store.commit("updateGame", data.game);
                }
                else if (data.event === "move") {
                    console.log(data);
                    // const game = store.state.pk.gameObject;
                    // const [snake0, snake1] = game.snakes;
                    // snake0.set_direction(data.a_direction);
                    // snake1.set_direction(data.b_direction);
                } else if (data.event === "result") {
                    console.log(data);
                    const game = store.state.pk.gameObject;
                    game;
                    // const [snake0, snake1] = game.snakes;

                    if (data.loser === "all" || data.loser === "A") {
                        // snake0.status = "die";
                    }
                    if (data.loser === "all" || data.loser === "B") {
                        // snake1.status = "die";
                    }
                    store.commit("updateLoser", data.loser);
                }

            }

            socket.onclose = () => {
                console.log("断开连接");
            }
        });

        onUnmounted(() => {
            socket.close();
            store.commit("updateStatus", "matching");
        });
    }
}   
</script>

<style scoped>

</style>