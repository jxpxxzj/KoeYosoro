<template>
    <div class="pinyin">
        <div class="main">
            <div class="py">
                {{ pinyin }}
            </div>
            <div class="word" v-html="text">
                
            </div>
        </div>

        <div class="cont">
            <mu-float-button class="record-icon" :icon="mic" @click="record"/>
        </div>

        <mu-paper class="func-area">
            <div class="text">
                <span style="color:#4caf50; font-weight:bold;">Correct: {{ correct }}</span>&nbsp; &nbsp; &nbsp;<span style="color: #f44336">Wrong: {{ wrong }}</span>
            </div>
            <div class="next-btn">
                <mu-raised-button label="Skip">
                </mu-raised-button>
                <mu-raised-button label="Next" primary @click="next">
                </mu-raised-button>
            </div>
        </mu-paper>
    </div>
</template>

<script>
import pinyin from 'pinyin'
import axios from 'axios'
import urls from '../urls';
import parseResult from '../text2html';
import MediaStreamRecorder from 'msr';

var divide = (text) => pinyin(text, {
        style: pinyin.STYLE_TONE
    }).map(t => t[0]).join(' ');


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

async function Vec(Difficulty, payload) {
    var formData = createFormData(payload);
    return await axios({
        method: 'POST',
        url: urls.voc,

        headers: {
            Difficulty,
        },
        data: formData,
        withCredentials: true
    });
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
            text: '',
            pinyin: '',
            correct: 0,
            wrong: 0,
            difficulty: 0,
            mic: 'mic'
        }
    },
    created() {
        this.next(null);
    },
    methods: {
        onTextChange(text, runDivide = true) {
            this.text = text;
            if(runDivide)
                this.pinyin = divide(text);
        },
        record() {
            if (this.mic === 'mic') { // not recording
                this.mic = "stop";
                mediaRecorder.ondataavailable = (blob) => {
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
            var result = await Vec(this.difficulty, blob);

            if (blob == null) {
                this.onTextChange(result.data.next);
                return;
            }
            var data = parseResult(result.data.result);
            
            if (data.allCorrect === true) {
                this.correct++;
            } else {
                this.wrong++;
            }
            this.onTextChange(data.html, false);
            console.log(data.html);
            setTimeout(this.onTextChange(result.data.next), 3000);
        },
        async init() {
            var result = await Vec(this.difficulty, null);
            this.onTextChange(result.data.reduce((pre, curr) => {
                return pre + curr.character;
            }));
        }
    }
}
</script>
<style scoped>
.pinyin {
    height: 100%;
    overflow-y: hidden;
}
.cont {
    text-align: center;
    width: 100%;
    position: fixed;
    bottom: 50px;
    margin-left: 0;
    margin-right: 0;
}
.func-area {
    position: fixed;
    bottom: 0;
    height: 36px;
    line-height: 36px;
    width: 100%;
}
.text {
    float: left;
    margin-left: 20px;
}
.next-btn {
    float: right;
}
.main {
    text-align: center;
    margin-top: 200px;
}
.word {
    font-size: 45px;
}
.py {
    font-size: 40px;
}

</style>

