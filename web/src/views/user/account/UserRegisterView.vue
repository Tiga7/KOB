<template>
    <CardView>
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent="register">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="password"
                            placeholder="请输入密码">
                    </div>
                    <div class="mb-3">
                        <label for="confirmerpwd" class="form-label">重复密码</label>
                        <input v-model="confirmedpwd" type="password" class="form-control" id="confirmerpwd"
                            placeholder="请再次输入密码">
                    </div>

                    <div class="error-message mb-3">{{ error_message }}</div>
                    <button type="submit" class="btn btn-primary">提交</button>
                </form>
            </div>
        </div>
    </CardView>
</template>

<script>
import CardView from '@/components/CardView.vue';
import { ref } from 'vue'
import router from '@/router';
import $ from 'jquery'

export default {
    components: {
        CardView
    },
    setup() {
        let username = ref('');
        let password = ref('');
        let confirmedpwd = ref('');
        let error_message = ref('');

        const register = () => {
            $.ajax({
                url: "http://localhost:3030/user/register/",
                type: 'post',
                data: {
                    username: username.value,
                    password: password.value,
                    confirmedPwd: confirmedpwd.value
                },
                success(resp) {
                    console.log(resp)
                    if (resp.message === 'success') {
                        router.push({ name: "home" })
                    } else {
                        error_message.value = resp.message;
                    }
                },
                error(resp) {
                    console.log(resp)
                }
            })
        }
        return {
            username,
            password,
            confirmedpwd,
            error_message,
            register,
        }

    },
}
</script>


<style scoped>
button {
    width: 100%;
}

div.error-message {
    color: red;
}
</style>