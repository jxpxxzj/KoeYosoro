<template>
    <div class="train">
        <div class="list">
            <mu-list>
                <mu-list-item v-for="(item, index) in message" :key="index" :class="item.class">
                    <mu-avatar :slot="item.slot" icon="folder"/> 
                    <div v-html="item.data"></div>
                </mu-list-item>
            </mu-list>
        </div>
        <div class="cont">
            <mu-float-button class="record-icon" :icon="mic" @click="record"/>
        </div>
         <mu-row class="scene">
            <mu-col width="33" tablet="33" desktop="33">
                <mu-flat-button label="场景1" class="full-btn">
                </mu-flat-button>
            </mu-col>
            <mu-col width="33" tablet="33" desktop="33">
                <mu-flat-button label="场景2" class="full-btn">
                </mu-flat-button>
            </mu-col>
            <mu-col width="33" tablet="33" desktop="33">
                <mu-flat-button label="场景3" class="full-btn">
                </mu-flat-button>
            </mu-col>
        </mu-row>
    </div>
</template>
<script>
import axios from 'axios'
import urls from '../urls';
import parseResult from '../text2html';
import MediaStreamRecorder from 'msr';

const message = [
    {
        slot:'leftAvatar',
        data: '<span style="font-size:16px">sentence</span>',
        class: '',
        username: 'Server'
    }, {
        slot: 'rightAvatar',
        data: '<span style="font-size:16px">TestText</span>',
        class: 'right-text',
        username: 'User name'
    }, {
        slot: 'leftAvatar',
        data: '<span style="font-size:16px">sentence2</span>',
        class: '',
        username: 'Server'
    }
];

async function Sen(Difficulty, payload) {
    var formData = createFormData(payload);
    return await axios({
        method: 'POST',
        url: urls.sen,

        headers: {
            Difficulty,
        },
        data: formData,
        withCredentials: true
    });
}

function createFormData(blob) {
    var file = new File([blob], 'msr-' + (new Date).toISOString().replace(/:|\./g, '-') + '.wav', {
        type: 'audio/wav'
    });

    // create FormData
    var formData = new FormData();
    formData.append('audio-filename', file.name);
    formData.append('audio-blob', file);

    return formData;
}

var mediaConstraints = {
    audio: true
};

navigator.getUserMedia(mediaConstraints, onMediaSuccess, onMediaError);
var mediaRecorder;
function onMediaSuccess(stream) {
    mediaRecorder = new MediaStreamRecorder(stream);
    mediaRecorder.mimeType = 'audio/wav'; // check this line for audio/wav
    
}

function onMediaError(e) {
    console.error('media error', e);
}

export default {
    data() {
        return {
            message,
            mic: 'mic'
        };
    },
    created() {
        this.init();
    },
    methods: {
        onTextChange(html, sender, type) {
            console.log(html);
            this.message.push({
                slot: type === "user" ? "rightAvatar" : "leftAvatar",
                data: html,
                class: type === "user" ? "right-text" : "",
                username: sender
            });
        },
        record() {
            if (this.mic === 'mic') { // not recording
                this.mic = "stop";
                mediaRecorder.ondataavailable = (blob) => {
                    this.onTextChange('<span style="font-size:16px">my voice</span>', "User name", "user");
                    this.next(blob);
                };
                mediaRecorder.start(100000);
                return;
            }

            if (this.mic === "stop") {
                mediaRecorder.stop();
                this.mic = "mic";
            }
        },
        async next(blob) {
            var result = await Sen(this.difficulty, blob);
            var data = parseResult(result.data.result, "16px");
            setTimeout(this.onTextChange(result.data.next, "Muskmelon", "server"), 3000);
        },
        async init() {
            var result = await Sen(this.difficulty, null);
            this.onTextChange(result.data.next);
        }
    }
}
</script>
<style lang="css">
.train div[class*="col-"] {
  background: #fff;
  text-align: center;
  color: #000;
  border: 1px solid #ddd;
  padding: 0px;
  height: 100px;
  line-height: 100px;
}
</style>
<style scoped>
.train {
    text-align: center;
}
.right-text {
    text-align: right;
}
.list {
    text-align: left;
    margin-bottom: 110px;
}
.scene {
    position: fixed;
    bottom: 0;
    width: 100%;
}
.cont {
    text-align: center;
    width: 100%;
    position: fixed;
    bottom: 120px;
    margin-left: 0;
    margin-right: 0;
}
.train .full-btn {
    
    height: 100px;
    width: 100%;
}
</style>