<template>
    <div>
        <CardView>
            user bot view
        </CardView>
    </div>
</template>

<script>
import CardView from '@/components/CardView.vue';
import $ from 'jquery'
import { useStore } from 'vuex'
export default {
    components: { CardView },
    setup() {
        const store = useStore();
        // $.ajax({
        //     url: "http://localhost:3030/user/bot/add",
        //     type: "post",
        //     data: {
        //         title: "我的bot",
        //         description: "111111",
        //         content: "代码列表"
        //     },
        //     headers: {
        //         authorization: "Bearer " + store.state.user.token,
        //     },
        //     success(resp) {
        //         console.log(resp);
        //     },
        //     error(resp) {
        //         console.log(resp);
        //     }
        // })
        // $.ajax({
        //     url: "http://localhost:3030/user/bot/remove",
        //     type: "post",
        //     data: {
        //         bot_id: ""
        //     },
        //     headers: {
        //         authorization: "Bearer " + store.state.user.token,
        //     },
        //     success(resp) {
        //         console.log(resp);
        //     },
        //     error(resp) {
        //         console.log(resp);
        //     }
        // })
        // $.ajax({
        //     url: "http://localhost:3030/user/bot/update",
        //     type: "post",
        //     data: {
        //         bot_id: "7",
        //         title: "修改bot",
        //         description: "2222222",
        //         content: "cppcpppcpppcpppcppcpc"
        //     },
        //     headers: {
        //         authorization: "Bearer " + store.state.user.token,
        //     },
        //     success(resp) {
        //         console.log(resp);
        //     },
        //     error(resp) {
        //         console.log(resp);
        //     }
        // })
        $.ajax({
            url: "http://localhost:3030/user/bot/botlist/",
            type: "get",
            headers: {
                authorization: "Bearer " + store.state.user.token,
            },
            success(resp) {
                console.log(resp);
            },
            error(resp) {
                console.log(resp);
            }
        })
    }
}
</script>

<style scoped>

</style>