<template>
    <NavBar />
    <router-view></router-view>

</template>

<script>
import NavBar from './components/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"

// import $ from 'jquery'

export default {
    components: {
        NavBar
    },
    setup() {

        // $.ajax({
        //     url: "http://localhost:3030/user/getInfo/",
        //     type: "get",
        //     headers: {
        //         authorization: "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0NmM4M2U5ZDJkMTQ0ZjVkODBlOTdlNWMxNjkyMGEyZiIsInN1YiI6Ii03ODMzNTU5MDMiLCJpc3MiOiJzZyIsImlhdCI6MTY1OTMzMTk4MSwiZXhwIjoxNjU5OTM2NzgxfQ.gc5YXzw--yasHcK3FY390BpjW_otPf7CSWL-yORQTxA"
        //     },
        //     success(resp) {
        //         console.log(resp)
        //     },
        //     error(resp) {
        //         console.log(resp)
        //     }
        // })

    }


}

</script>

<style>
body {
    background: url("@/assets/image/3c2.png");
    background-size: cover;
}
</style>