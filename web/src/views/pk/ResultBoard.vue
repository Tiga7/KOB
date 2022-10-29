<template>
    <div class="result-board">
        <div class="result-board-text" v-if="$store.state.pk.loser === 'all'">
            平局
        </div>
        <div class="result-board-text"
            v-else-if="$store.state.pk.loser === 'A' && $store.state.pk.a_id == $store.state.user.id">
            失败了
        </div>
        <div class="result-board-text"
            v-else-if="$store.state.pk.loser === 'B' && $store.state.pk.b_id == $store.state.user.id">
            失败了
        </div>
        <div class=" result-board-text" v-else>
            我们是冠军
        </div>
        <div class="result-board-btn">
            <button type="button" class="btn btn-primary btn-lg" @click="restart">
                再来一把!
            </button>
        </div>

    </div>
</template>

<script>
import { useStore } from 'vuex';

export default {
    setup() {

        const store = useStore();
        const restart = () => {
            store.commit("updateStatus", "matching");
            store.commit("updateLoser", "none");
            store.commit("updateOpponent", {
                username: "我的对手",
                photo: "https://img2.baidu.com/it/u=2602524975,4281341375&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400",
            })

        }
        return {
            restart
        }
    }

}

</script>

<style scoped>
div.result-board {
    height: 30vh;
    width: 30vw;
    background-color: rgba(50, 50, 50, 0.5);
    position: absolute;
    top: 30vh;
    left: 35vw;
}

div.result-board-text {
    text-align: center;
    color: aliceblue;
    font-size: 55px;
    font-weight: 600;
    padding-top: 3vh;
}

div.result-board-btn {
    padding-top: 3vh;
    text-align: center;
}
</style>