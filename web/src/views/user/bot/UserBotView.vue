<template>
    <div class="container">
        <div class="row">
            <div class="col-3">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-body">
                        <img :src="$store.state.user.photo" alt="" style="width: 100%;">
                    </div>
                </div>
            </div>
            <div class="col-9">

                <div class="card" style="margin-top: 20px;">
                    <div class="card-header">
                        <span style="font-size: 130%">我的Bot</span>
                        <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal"
                            data-bs-target="#add_bot_btn">
                            增加Bot
                        </button>
                        <!-- add bot Modal -->
                        <div class="modal fade" id="add_bot_btn" data-bs-backdrop="static" data-bs-keyboard="false"
                            tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                            <div class="modal-dialog modal-lg">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title">增加Bot</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                            aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">

                                        <div class="mb-3">
                                            <label for="bot_title" class="form-label">标题</label>
                                            <input v-model="bot_for_add.title" type="text" class="form-control"
                                                id="bot_title" placeholder="请输入Bot标题">
                                        </div>
                                        <div class="mb-3">
                                            <label for="bot_description" class="form-label">
                                                描述
                                            </label>
                                            <textarea v-model="bot_for_add.description" class="form-control"
                                                id="bot_description" rows="3" placeholder="请输入Bot描述"></textarea>
                                        </div>
                                        <div class="mb-3">
                                            <label for="bot_content" class="form-label">
                                                代码
                                            </label>
                                            <VAceEditor v-model:value="bot_for_add.content" @init="editorInit"
                                                lang="c_cpp" theme="textmate" style="height: 300px" />

                                        </div>

                                    </div>
                                    <div class="modal-footer">
                                        <div class="error-message">
                                            {{ bot_for_add.error_message }}
                                        </div>
                                        <button type="button" class="btn btn-primary" @click="add_bot">确认增加</button>
                                        <button type="button" class="btn btn-secondary"
                                            data-bs-dismiss="modal">关闭</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="card-body">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th scope="col">序号</th>
                                    <th scope="col">标题</th>
                                    <th scope="col">描述</th>
                                    <th scope="col">创建时间</th>
                                    <th scope="col">操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="(item, index) in bots" :key="item.id">
                                    <td>{{ index+1 }}</td>
                                    <td>{{ item.title }}</td>
                                    <td>{{ item.description }}</td>
                                    <td>{{ item.createTime }}</td>
                                    <td>
                                        <button type="button" class="btn btn-secondary" style="margin-right:10px;"
                                            data-bs-toggle="modal" :data-bs-target="'#update-bot-btn-'+item.id">
                                            修改
                                        </button>

                                        <!-- update bot Modal -->
                                        <div class="modal fade" :id="'update-bot-btn-'+item.id"
                                            data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
                                            aria-labelledby="staticBackdropLabel" aria-hidden="true">
                                            <div class="modal-dialog modal-lg">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title">修改Bot</h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                            aria-label="Close"></button>
                                                    </div>
                                                    <div class="modal-body">

                                                        <div class="mb-3">
                                                            <label for="bot_title" class="form-label">标题</label>
                                                            <input v-model="item.title" type="text" class="form-control"
                                                                id="bot_title" placeholder="请输入Bot标题">
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="bot_description" class="form-label">
                                                                描述
                                                            </label>
                                                            <textarea v-model="item.description" class="form-control"
                                                                id="bot_description" rows="3"
                                                                placeholder="请输入Bot描述"></textarea>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="bot_content" class="form-label">
                                                                代码
                                                            </label>
                                                            <VAceEditor v-model:value="item.content" @init="editorInit"
                                                                lang="c_cpp" theme="textmate" style="height: 300px" />
                                                        </div>

                                                    </div>
                                                    <div class="modal-footer">
                                                        <div class="error-message">
                                                            {{ item.error_message }}
                                                        </div>
                                                        <button type="button" class="btn btn-primary"
                                                            @click="update_bot(item)">保存修改</button>
                                                        <button type="button" class="btn btn-secondary"
                                                            data-bs-dismiss="modal" @click="refresh_bot">关闭</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <button type="button" class="btn btn-danger" data-bs-toggle="modal"
                                            data-bs-target="#remove-bot-btn">删除</button>
                                        <!-- remove bot Modal -->
                                        <div class="modal fade modal-centered" id="remove-bot-btn"
                                            data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
                                            aria-labelledby="staticBackdropLabel" aria-hidden="true">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title">删除 Bot</h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                            aria-label="Close"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        确认删除此条Bot吗?
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-primary"
                                                            @click="remove_bot(item)">确认删除</button>
                                                        <button type="button" class="btn btn-secondary"
                                                            data-bs-dismiss="modal">关闭</button>

                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                    </td>
                                </tr>
                            </tbody>
                        </table>



                    </div>


                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { ref, reactive } from "vue";
import $ from 'jquery'
import { useStore } from 'vuex'
import { Modal } from 'bootstrap/dist/js/bootstrap'
import { VAceEditor } from 'vue3-ace-editor'
import ace from 'ace-builds'

export default {
    components: {
        VAceEditor
    },
    setup() {

        //编辑器配置
        ace.config.set(
            "basePath",
            "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")

        const store = useStore();
        //从后端获取bot list
        let bots = ref([]);

        const bot_for_add = reactive({
            title: "",
            description: "",
            content: "",
            error_message: "",
        });
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

        const add_bot = () => {
            bot_for_add.error_message = "",
                $.ajax({
                    url: "http://localhost:3030/user/bot/add",
                    type: "post",
                    data: {
                        title: bot_for_add.title,
                        description: bot_for_add.description,
                        content: bot_for_add.content,
                    },
                    //验证
                    headers: {
                        authorization: "Bearer " + store.state.user.token,
                    },
                    success(resp) {
                        if (resp.message === "添加Bot成功") {
                            refresh_bot();
                            bot_for_add.title = "";
                            bot_for_add.description = "";
                            bot_for_add.content = "";
                            Modal.getInstance("#add_bot_btn").hide();
                        } else {
                            bot_for_add.error_message = resp.message;
                        }
                    },
                    error(resp) {
                        bot_for_add.error_message = resp.message;
                    }
                })
        }
        const remove_bot = (bot) => {
            $.ajax({
                url: "http://localhost:3030/user/bot/remove",
                type: "post",
                data: {
                    bot_id: bot.id,
                },
                headers: {
                    authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if (resp.message === "删除bot成功") {
                        refresh_bot();
                        Modal.getInstance("#remove-bot-btn").hide();
                    } else {
                        console.log(resp);
                    }
                }
            })
        }

        const update_bot = (bot) => {
            bot_for_add.error_message = "",
                $.ajax({
                    url: "http://localhost:3030/user/bot/update",
                    type: "post",
                    data: {
                        bot_id: bot.id,
                        title: bot.title,
                        description: bot.description,
                        content: bot.content,
                    },
                    //验证
                    headers: {
                        authorization: "Bearer " + store.state.user.token,
                    },
                    success(resp) {
                        console.log(resp);
                        if (resp.message === "修改Bot成功") {
                            refresh_bot();
                            Modal.getInstance('#update-bot-btn-' + bot.id).hide();
                        } else {
                            bot.error_message = resp.message;
                        }
                    },
                    error(resp) {
                        bot.error_message = resp.message;
                    }
                })
        }

        return {
            bots,
            bot_for_add,
            refresh_bot,
            add_bot,
            remove_bot,
            update_bot,
        }
    }
}
</script>

<style scoped>
div.error-message {
    color: red;
}
</style>