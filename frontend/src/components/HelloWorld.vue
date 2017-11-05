<template>
    <div class="hello">
        <mu-paper class="user-panel" :zDepth="1">
            <mu-list-item style="width: 100%" title="User name">
                <mu-avatar icon="folder" slot="leftAvatar"/>
            </mu-list-item>
        </mu-paper>
        <mu-paper class="main-func"  :zDepth="1">
            <mu-row>
                <mu-col width="50" tablet="50" desktop="50">
                    <mu-flat-button class="full-btn"  @click="navigateTo('pinyin')">
                        <img src="../assets/Vab.png" class="img">
                    </mu-flat-button>
                </mu-col>
                <mu-col width="50" tablet="50" desktop="50">
                    <mu-flat-button class="full-btn" @click="navigateTo('train')">
                        <img src="../assets/Scene.png" class="img">
                    </mu-flat-button>
                </mu-col>       
            </mu-row>
            <mu-row>
                <mu-col width="50" tablet="50" desktop="50">
                    <mu-flat-button class="full-btn">
                        <img src="../assets/Speaking.png" class="img">
                    </mu-flat-button>
                </mu-col>
                <mu-col width="50" tablet="50" desktop="50">
                    <mu-flat-button class="full-btn">
                        <img src="../assets/Listener.png" class="img">
                    </mu-flat-button>
                </mu-col>
            </mu-row>
            <mu-row>
                <mu-col width="50" tablet="50" desktop="50">
                    <mu-flat-button class="full-btn">
                        <img src="../assets/Random.png" class="img">
                    </mu-flat-button>
                </mu-col>
                <mu-col width="50" tablet="50" desktop="50">
                    <mu-flat-button class="full-btn">
                        <img src="../assets/Person.png" class="img">
                    </mu-flat-button>
                </mu-col>
            </mu-row>
        </mu-paper>
    </div>
</template>

<script>
export default {
  name: 'HelloWorld',
  methods:{
      navigateTo(name) {
          this.$router.push(`/${name}`);
      }
  }
}
</script>


<style lang="css">
.hello div[class*="col-"] {
  background: #fff;
  text-align: center;
  color: #000;
  padding: 0px;
  height: 180px;
  line-height: 180px;
}
</style>
<style scoped>
.main-func {
    margin-top: 5px;
}
.hello {
    background-color: #e8e8e8;
    height: 100%;
    margin-top: 5px;
}
.img {
    width: 100%;
    height:100%;
}
.user-panel {
    width: 100%;
    background-color: #fff;
}
.hello .full-btn {
    text-align: center;
    height: 180px;
    width: 100%;
}
</style>