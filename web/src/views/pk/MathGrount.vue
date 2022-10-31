<template>
    <div class="matchground">
        <div class="row">
            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.user.photo" alt="头像">
                </div>
                <div class="user-username">
                    {{ $store.state.user.username }}
                </div>
            </div>
            <div class="col-4">
                <div class="select-bot">
                    <select v-model="select_bot" class="form-select">
                        <option value="-1" selected>亲自上阵</option>
                        <option v-for="bot in bots" :key="bot.id" :value="bot.id">
                            {{ bot.title }}
                        </option>
                    </select>
                </div>
            </div>
            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.pk.opponent_photo" alt="对手头像">
                </div>
                <div class="user-username">
                    {{ $store.state.pk.opponent_username }}
                </div>
            </div>
            <div class="col-12" style="text-align:center; padding-top: 15vh;">
                <button type="button" class="btn btn-warning btn-lg" @click="click_match_btn">
                    {{ match_btn_info }}
                </button>
            </div>

        </div>
    </div>
</template>

<script>
import { ref } from 'vue'
import { useStore } from 'vuex'
import $ from 'jquery'
export default {
    setup() {

        const store = useStore();
        const bots = ref([]);
        const select_bot = ref("-1");

        let match_btn_info = ref("开始匹配");
        const click_match_btn = () => {

            if (match_btn_info.value == "开始匹配") {
                match_btn_info.value = "取消匹配";
                store.state.pk.socket.send(JSON.stringify({
                    event: "start-matching",
                    bot_id: select_bot.value
                }));
            } else {
                match_btn_info.value = "开始匹配";
                //JSON.stringify({}); 封装成字符串
                store.state.pk.socket.send(JSON.stringify({
                    event: "stop-matching",
                }));
            }
        }

        const refresh_bot = () => {
            $.ajax({
                url: "http://localhost:3030/user/bot/botlist",
                type: "get",
                headers: {
                    authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    bots.value = resp;
                },
                error() {
                }
            })
        }
        refresh_bot();
        return {
            match_btn_info,
            bots,
            select_bot,
            click_match_btn
        }
    }
}
</script>

<style scoped>
div.matchground {
    width: 60vw;
    height: 70vh;
    margin: 40px auto;
    background-color: rgba(50, 50, 50, 0.5);
}

div.user-photo {
    text-align: center;

}

div.user-photo>img {
    /* padding-top: 5vh; */
    margin-top: 5vh;
    border-radius: 50%;
    width: 20vh;
}

div.user-username {
    margin-top: 5px;
    text-align: center;
    font-size: 30px;
    font-weight: 600;
    color: white;
}

div.select-bot {
    padding-top: 8vh;
}

div.select-bot>select {
    width: 70%;
    margin: 0 auto;
}
</style>