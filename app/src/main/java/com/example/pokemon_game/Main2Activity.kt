package com.example.pokemon_game

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main2.*
class Main2Activity : AppCompatActivity() {
    private var path: MutableList<Pokemon> = mutableListOf()
    private var player = MediaPlayer()
    private var tableWidth = 12
    private var tableHeight = 6
    private var level = 5

    private var awardList = arrayListOf("增加分數","增加洗牌","增加提示","...沒有東西")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        level = intent.getIntExtra("level",5)
        var selectPokemonA = Pokemon()
        var selectPokemonB = Pokemon()
        var bounceAnim = AnimationUtils.loadAnimation(this,R.anim.bounce)
        var combeCount = 0
        var combeCountMax = 0
        var combeScore = 0
        var time = 180
        var win = false
        var pokemonTable = createPokemonTable()
        var buttonTable = createTable(pokemonTable)
        var pokemonBallButtonTable = createPokemonButtonTable()
        var tips = 3
        var refresh = 3
        var score = 0
        tip_number.text = tips.toString()
        refresh_number.text = refresh.toString()
        timeProgressBar.max = time
        timeProgressBar.setProgress(time, false)
        var progressTimer = object:CountDownTimer(time.toLong()*1000,1000){
            override fun onFinish() {
                if(!win) {
                    playSound("gameover")
                    popLayoutBackground.alpha = 0.5f
                    gameOverPopLayout.alpha = 1f
                    restartButton.visibility = View.VISIBLE
                    tip_button.isEnabled = false
                    refresh_button.isEnabled = false
                    for (i in 0..tableHeight - 1) {
                        for (j in 0..tableWidth - 1) {
                            buttonTable[i][j].isEnabled = false
                        }
                    }
                }else{
                    win = false
                    this.start()
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                if (time > 0 && !win) {
                    time -= 1
                    timeProgressBar.setProgress(time, true)
                    time_number.text = time.toString()
                }
            }
        }.start()

        Log.d("init","init在這裡")
        tip_button.setOnClickListener {
            //執行提示
            if (tips > 0) {
                var (tmptipsA, tmptipsB) = findTips(pokemonTable)
                buttonTable[tmptipsA.y!!][tmptipsA.x!!].startAnimation(bounceAnim)
                buttonTable[tmptipsB.y!!][tmptipsB.x!!].startAnimation(bounceAnim)
                tips-=1
                tip_number.text = tips.toString()
                playSound("spell")
            }
        }
        refresh_button.setOnClickListener {
            //執行refresh
            if(refresh > 0) {
                selectPokemonA = Pokemon()
                selectPokemonB = Pokemon()
                pokemonTable = shuffleTable(pokemonTable)
                for (i in 0..tableHeight - 1) {
                    for (j in 0..tableWidth - 1) {
                        buttonTable[i][j].setBackgroundResource(getPokemonImage(pokemonTable[i][j]))
                        buttonTable[i][j].alpha = 1f
                        buttonTable[i][j].scaleX = 1f
                        buttonTable[i][j].scaleY = 1f
                    }
                }
                playSound("spell")
                refresh -=1
                refresh_number.text = refresh.toString()
            }
        }
        nextButton.setOnClickListener {
            playSound("press")
            resultPopLayout.alpha = 0f
            awardPopLayout.alpha = 1f
            nextButton.visibility = View.INVISIBLE
            continueButton.visibility = View.VISIBLE
            for (i in 0..2){pokemonBallButtonTable[i].visibility = View.VISIBLE}
        }
        continueButton.setOnClickListener {
            playSound("press")
            popLayoutBackground.alpha = 0f
            awardPopLayout.alpha = 0f
            awardLabel.alpha = 0f
            continueButton.visibility = View.INVISIBLE
            awardText.alpha = 0f
            tip_button.isEnabled = true
            refresh_button.isEnabled = true
            level+=1
            time = 180
            progressTimer.cancel()
            progressTimer.onFinish()
            for (i in 0..2){pokemonBallButtonTable[i].visibility = View.INVISIBLE;pokemonBallButtonTable[i].setBackgroundResource(R.drawable.ball1)}
            pokemonTable = createPokemonTable()
            for (i in 0..tableHeight - 1) {
                for (j in 0..tableWidth - 1) {
                    buttonTable[i][j].setBackgroundResource(getPokemonImage(pokemonTable[i][j]))
                    buttonTable[i][j].alpha = 1f
                    buttonTable[i][j].scaleX = 1f
                    buttonTable[i][j].scaleY = 1f
                    buttonTable[i][j].isEnabled = true
                }
            }
        }
        restartButton.setOnClickListener {
            playSound("press")
            pokemonTable = createPokemonTable()
            popLayoutBackground.alpha = 0f
            gameOverPopLayout.alpha = 0f
            restartButton.visibility = View.INVISIBLE
            tip_button.isEnabled = true
            refresh_button.isEnabled = true
            time = 180
            timeProgressBar.max = time
            progressTimer.start()
            for (i in 0..tableHeight - 1) {
                for (j in 0..tableWidth - 1) {
                    buttonTable[i][j].setBackgroundResource(getPokemonImage(pokemonTable[i][j]))
                    buttonTable[i][j].alpha = 1f
                    buttonTable[i][j].scaleX = 1f
                    buttonTable[i][j].scaleY = 1f
                    buttonTable[i][j].isEnabled = true
                }
            }
        }
        for (i in 0..tableHeight-1) {
            for (j in 0..tableWidth-1) {
                buttonTable[i][j].setOnClickListener {
                    if (pokemonTable[i][j].name == "無") {
                        if(selectPokemonA.name != null) {
                            buttonTable[selectPokemonA.y!!][selectPokemonA.x!!].animate()
                                .scaleX(1.0f).scaleY(1.0f).setDuration(300)
                        }
                        selectPokemonA = Pokemon()
                        selectPokemonB = Pokemon()
                    } else {
                        if (selectPokemonA.name != null) {
                            selectPokemonB.name = pokemonTable[i][j].name
                            selectPokemonB.x = j
                            selectPokemonB.y = i
                            Log.d("pokeA",selectPokemonA.name+"")
                            Log.d("pokeB",selectPokemonB.name+"")
                            if (selectPokemonA.name == selectPokemonB.name && (selectPokemonA.x != selectPokemonB.x || selectPokemonA.y != selectPokemonB.y)) {
                                Log.d("findpath","找路近")
                                if(findPath(nodeX = selectPokemonA.x!!,nodeY = selectPokemonA.y!!,destinationX = selectPokemonB.x!!,destinationY = selectPokemonB.y!!,
                                        Table = pokemonTable,getPath = true)){
                                    for (item in path){
                                        Log.d("itemName",item.name.toString())
                                        Log.d("itemX",item.x.toString())
                                        Log.d("itemY",item.y.toString())
                                        buttonTable[item.y!!][item.x!!].alpha = 1f
                                        buttonTable[item.y!!][item.x!!].setBackgroundResource(getDirectionImage(item,pokemonTable[item.y!!][item.x!!]))
                                        buttonTable[item.y!!][item.x!!].animate().alpha(0f).setDuration(500)
                                        pokemonTable[item.y!!][item.x!!].name = "無"
                                    }
                                    score_number.text = score.toString()
                                    win = Win(pokemonTable)
                                    if(win){
                                        popLayoutBackground.alpha = 0.5f
                                        resultPopLayout.alpha = 1f
                                        nextButton.visibility = View.VISIBLE
                                        tip_button.isEnabled = false
                                        refresh_button.isEnabled = false
                                        score_number.text = score.toString()
                                        timePopLayout.text = (time*100).toString()
                                        totalScorePopLayout.text = (time*100+score).toString()
                                        comboNumberPopLayout.text = combeCountMax.toString()
                                        comboScorePopLayout.text = combeScore.toString()
                                        score += time * 100 + combeScore
                                        score_number.text = score.toString()
                                        //nextButton.text = "繼續"
                                        playSound("victory")
                                        for (i in 0..tableHeight - 1) {
                                            for (j in 0..tableWidth - 1) {
                                                buttonTable[i][j].isEnabled = false
                                            }
                                        }
                                    }else {
                                        playSound("hit")
                                    }
                                    combeCount += 1   //   連擊相關加分設定
                                    if (combeCount >= 2) {
                                        combeScore += 200
                                        score = score + 100 + 200
                                    }else {
                                        score = score + 100
                                    }
                                    if (combeCount >= combeCountMax) {
                                        combeCountMax = combeCount
                                    }
                                }else{//沒找到路徑
                                    Log.d("path","沒路近")
                                    buttonTable[selectPokemonA.y!!][selectPokemonA.x!!].animate().scaleX(1.0f).scaleY(1.0f).setDuration(300)
                                    playSound("cancel")
                                }
                            }else{//名字不相同或者點選到同一隻
                                Log.d("名字不相同A",selectPokemonA.name.toString())
                                Log.d("名字不相同B",selectPokemonB.name.toString())
                                playSound("cancel")
                                buttonTable[selectPokemonA.y!!][selectPokemonA.x!!].animate().scaleX(1.0f).scaleY(1.0f).setDuration(300)
                            }
                            selectPokemonA = Pokemon()
                            selectPokemonB = Pokemon()
                        } else {
                            playSound("select")
                            selectPokemonA.name = pokemonTable[i][j].name
                            selectPokemonA.x = j
                            selectPokemonA.y = i
                            buttonTable[i][j].animate().scaleX(1.25f).scaleY(1.25f).setDuration(300)
                            path = mutableListOf()
                            Log.i("平常",pokemonTable.toString())
                        }
                    }
                }
            }
        }
        for (i in 0..2){
            pokemonBallButtonTable[i].setOnClickListener {
                pokemonBallButtonTable[i].setBackgroundResource(R.drawable.ball3)
                for(j in 0..2){
                    if(j!=i){
                        pokemonBallButtonTable[j].visibility = View.INVISIBLE
                    }
                }
                awardLabel.alpha = 1f
                awardText.alpha = 1f
                playSound("open")
                var award = awardList.random()
                awardText.text = award
                when (award) {
                    "增加分數" -> score += 3000
                    "增加提示" -> tips += 1
                    "增加洗牌" -> refresh += 1
                    "沒有東西" -> awardText.text = "...沒有東西"
                }
                refresh_number.text = refresh.toString()
                tip_number.text = tips.toString()
                score_number.text = score.toString()
            }
        }
    }
    private fun createPokemonButtonTable():MutableList<Button> {
        var pokemonBallButtonTable = mutableListOf<Button>()
        for (i in 0..2) {
            var tmpButton = Button(this)
            tmpButton.setBackgroundResource(R.drawable.ball1)
            tmpButton.layoutParams = TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )
            tmpButton.visibility = View.INVISIBLE
            pokemonBallButtonTable.add(tmpButton)
            awardLayout.addView(tmpButton)
        }
        return pokemonBallButtonTable
    }
    private fun createTable(
        pokemonTable: MutableList<MutableList<Pokemon>>
    ): MutableList<MutableList<Button>> {
        var buttonTable: MutableList<MutableList<Button>> = mutableListOf()
        for (i in 0..tableHeight - 1) {
            var tmpArray: MutableList<Button> = mutableListOf()
            var tmpTableRow: TableRow = TableRow(this)
            for (j in 0..tableWidth - 1) {
                var tmpButton = Button(this)
                tmpButton.setBackgroundResource(getPokemonImage(pokemonTable[i][j]))
                tmpButton.layoutParams = TableRow.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
                )
                tmpTableRow.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    12f
                )
                tmpTableRow.addView(tmpButton)
                tmpArray.add(tmpButton)
            }
            gameTable.addView(tmpTableRow)
            buttonTable.add(tmpArray)
        }
        return buttonTable
    }

    private fun createPokemonTable(
    ): MutableList<MutableList<Pokemon>> {
        val pokemonListLV1 =
            arrayListOf("皮卡丘", "伊布", "小火龍", "耿鬼", "妙娃種子", "百變怪", "卡比獸", "鯉魚王", "傑尼龜", "向尾喵")
        val pokemonListLV2 = arrayListOf(
            "皮卡丘",
            "伊布",
            "小火龍",
            "耿鬼",
            "妙娃種子",
            "百變怪",
            "卡比獸",
            "鯉魚王",
            "傑尼龜",
            "向尾喵",
            "小小象",
            "地鼠",
            "沼王",
            "仙子伊布"
        )
        val pokemonListLV3 = arrayListOf(
            "皮卡丘",
            "伊布",
            "小火龍",
            "耿鬼",
            "妙娃種子",
            "百變怪",
            "卡比獸",
            "鯉魚王",
            "傑尼龜",
            "向尾喵",
            "小小象",
            "地鼠",
            "沼王",
            "仙子伊布",
            "含羞苞",
            "正電拍拍",
            "火球鼠",
            "龍蝦小兵"
        )
        val pokemonListLV4 = arrayListOf(
            "皮卡丘",
            "伊布",
            "小火龍",
            "耿鬼",
            "妙娃種子",
            "百變怪",
            "卡比獸",
            "鯉魚王",
            "傑尼龜",
            "向尾喵",
            "小小象",
            "地鼠",
            "沼王",
            "仙子伊布",
            "含羞苞",
            "正電拍拍",
            "火球鼠",
            "龍蝦小兵",
            "哈克龍",
            "凱西",
            "六尾",
            "小拉達"
        )
        val pokemonList = when (level) {
            1 -> pokemonListLV1
            2 -> pokemonListLV2
            3 -> pokemonListLV3
            4 -> pokemonListLV4
            else-> pokemonListLV4
        }
        var pokemon1DTable: MutableList<Pokemon> = mutableListOf()
        var pokemon2DTable: MutableList<MutableList<Pokemon>> = mutableListOf()
        for (i in 0..tableHeight * tableWidth / 2 - 1 ) {
            var tmpPokemon = Pokemon()
            tmpPokemon.name = pokemonList[i % pokemonList.size]
            pokemon1DTable.add(tmpPokemon)
            tmpPokemon = Pokemon()
            tmpPokemon.name = pokemonList[i % pokemonList.size]
            pokemon1DTable.add(tmpPokemon)
        }
        pokemon1DTable.shuffle()
        var k = 0
        for (i in 0..tableHeight - 1) {
            pokemon2DTable.add(mutableListOf())
            for (j in 0..tableWidth - 1) {
                var tmpPokemon = pokemon1DTable[k]
                pokemon2DTable[i].add(tmpPokemon)
                k += 1
            }
        }
        return pokemon2DTable
    }

    private fun getPokemonImage(pokemon: Pokemon): Int {
        var id = when (pokemon.name) {
            "皮卡丘" -> R.drawable.pikachu
            "伊布" -> R.drawable.eevee
            "妙娃種子" -> R.drawable.bulbasaur
            "小火龍" -> R.drawable.charmander
            "耿鬼" -> R.drawable.gengar
            "百變怪" -> R.drawable.ditto
            "卡比獸" -> R.drawable.snorlax
            "鯉魚王" -> R.drawable.magikarp
            "傑尼龜" -> R.drawable.squirtle
            "向尾喵" -> R.drawable.skitty
            "小小象" -> R.drawable.phanpy
            "地鼠" -> R.drawable.diglett
            "沼王" -> R.drawable.quagsire
            "仙子伊布" -> R.drawable.sylveon
            "含羞苞" -> R.drawable.budew
            "正電拍拍"-> R.drawable.plusle
            "火球鼠" -> R.drawable.cyndaquil
            "龍蝦小兵" -> R.drawable.corphish
            "哈克龍" -> R.drawable.dragonair
            "凱西" -> R.drawable.abra
            "六尾" -> R.drawable.vulpix
            "小拉達" -> R.drawable.rattata
            else -> R.drawable.none
        }
        return id
    }

    private fun findPath(
        nodeX: Int,
        nodeY: Int,
        destinationX: Int,
        destinationY: Int,
        Table: MutableList<MutableList<Pokemon>>,
        nodes: MutableList<Pokemon> = mutableListOf(),
        times: Int = 0,
        direction: String = "NONE",
        getPath: Boolean
    ): Boolean {
        //初始化參數 & let to var
        var flag = false
        var directionList: MutableList<String> = mutableListOf()
        var newnodes = mutableListOf<Pokemon>()
        for (item in nodes){
            newnodes.add(item)
        }
        var node = Pokemon()
        node.name = "無"
        node.x = nodeX
        node.y = nodeY
        val containFlag = checkContain(nodes = nodes, node = node)
        if (times > 2 || nodeX < 0 || nodeX >= Table[0].size || nodeY < 0 || nodeY >= Table.size || containFlag) {
            //轉彎超過兩次或者節點超過表格大小以及該節點是否已經走過
            return false
        } else {
            //將當前 node 加入 nodes 陣列
            if (nodeX == destinationX && nodeY == destinationY) {
                newnodes.add(node)
                //如果當前這個位置抵達目的地
                newnodes[newnodes.size - 1].direction = "tail"
                if (getPath) {
                    path = newnodes
                }
                Log.d("finished","finished")
                return true
            } else if (Table[nodeY][nodeX].name.toString() != "無" && direction != "NONE") {
                //如果當前節點不可行走(初始起點不算)
                return false
            } else {
                newnodes.add(node)
                if (nodeX > destinationX && nodeY > destinationY) {
                    directionList = getDirectionList(location = "左上")
                }//若目的地在節點左上
                else if (nodeX > destinationX && nodeY < destinationY) {
                    directionList = getDirectionList(location = "左下")
                }//若目的地在節點左下
                else if (nodeX < destinationX && nodeY > destinationY) {
                    directionList = getDirectionList(location = "右上")
                }//若目的地在節點右上
                else if (nodeX < destinationX && nodeY < destinationY) {
                    directionList = getDirectionList(location = "右下")
                }//若目的地在節點右上
                else if (nodeX == destinationX && nodeY != destinationY) {
                    directionList = getDirectionList(location = "上下")
                }//若目的地在節點上下
                else if (nodeX != destinationX && nodeY == destinationY) {
                    directionList = getDirectionList(location = "左右")
                }//若目的地在節點左右
                for (i in 0..3) {
                    Log.d("direction",""+directionList)
                    Log.d("nodesSize",newnodes.size.toString())
                    var x = 0
                    var y = 0
                    if (directionList[i] == "LEFT") {
                        x = -1;y = 0
                    } else if (directionList[i] == "RIGHT") {
                        x = 1;y = 0
                    } else if (directionList[i] == "UP") {
                        x = 0;y = -1
                    } else if (directionList[i] == "DOWN") {
                        x = 0;y = 1
                    }
                    if (!flag) {
                        if (direction == directionList[i] || direction == "NONE") {
                            newnodes[newnodes.size - 1].direction = getDirectionNumber(
                                oldDirection = direction,
                                newDirection = directionList[i],
                                directionChanged = false
                            )
                            flag = findPath(
                                nodeX = nodeX + x,
                                nodeY = nodeY + y,
                                destinationX = destinationX,
                                destinationY = destinationY,
                                Table = Table,
                                nodes = newnodes,
                                times = times,
                                direction = directionList[i],
                                getPath = getPath
                            )
                        } else {
                            //如果將行走方向與原方向不同代表轉彎
                            newnodes[newnodes.size - 1].direction = getDirectionNumber(
                                oldDirection = direction,
                                newDirection = directionList[i],
                                directionChanged = true
                            )
                            flag = findPath(
                                nodeX = nodeX + x,
                                nodeY = nodeY + y,
                                destinationX = destinationX,
                                destinationY = destinationY,
                                Table = Table,
                                nodes = newnodes,
                                times = times + 1,
                                direction = directionList[i],
                                getPath = getPath
                            )
                        }
                        Log.d("flag",flag.toString())
                    }
                }
            }
        }
        return flag
    }
    private fun findTips(Table:MutableList<MutableList<Pokemon>>):Pair<Pokemon,Pokemon>{
        var selectA = Pokemon()
        var selectB = Pokemon()
        for (i in 0..tableHeight-1){
            for (j in 0..tableWidth-1){
                if(Table[i][j].name != "無"){
                    selectA.name = Table[i][j].name
                    selectA.x = j
                    selectA.y = i
                    for (k in 0..tableHeight-1){
                        for( m in 0..tableWidth-1){
                            if (selectA.name == Table[k][m].name && (i != k || j != m) ){
                                selectB.name = Table[k][m].name
                                selectB.x = m
                                selectB.y = k
                                if(findPath(nodeX = selectA.x!!, nodeY= selectA.y!!, destinationX = selectB.x!!, destinationY = selectB.y!!,
                                    Table = Table,getPath = false)){
                                    return Pair(selectA , selectB)
                                }
                            }
                        }
                    }
                }
            }
        }
        selectA = Pokemon()
        selectB = Pokemon()
        return Pair(selectA,selectB)
    }
    private fun shuffleTable(table: MutableList<MutableList<Pokemon>>) : MutableList<MutableList<Pokemon>>{
        var tmpTable:MutableList<Pokemon> = mutableListOf()
        var newTable:MutableList<MutableList<Pokemon>> = mutableListOf()
        for (i in 0..tableHeight-1){
            for (j in 0..tableWidth-1){
                tmpTable.add(table[i][j])
            }
        }
        //打散寶可夢的排序
        tmpTable.shuffle()
        //將打散好的寶可夢重新添入二維陣列
        var k = 0
        for (i in 0..tableHeight-1){
            newTable.add(mutableListOf())
            for (j in 0..tableWidth-1){
                var tmpPokemon = tmpTable[k]
                newTable[i].add(tmpPokemon)
                k+=1
            }
        }
        return newTable
    }
    private fun checkContain(nodes: MutableList<Pokemon>, node: Pokemon): Boolean {
        for (pokemon in nodes) {
            if (pokemon.x == node.x && pokemon.y == node.y){
                return true
            }
        }
        return false
    }
    private fun Win(Table:MutableList<MutableList<Pokemon>>):Boolean{
        for( i in Table){
            for( item in i ){
                if (item.name != "無"){
                    return false
                }
            }
        }
        level += 1      //過關難度增加
        return true
    }
    private fun getDirectionList(location: String): ArrayList<String> {
        //決定尋找方向的順序
        return when (location) {
            "右上" -> arrayListOf("UP", "RIGHT", "LEFT", "DOWN")
            "右下" -> arrayListOf("DOWN", "RIGHT", "LEFT", "UP")
            "左上" -> arrayListOf("UP", "LEFT", "RIGHT", "DOWN")
            "左下" -> arrayListOf("DOWN", "LEFT", "RIGHT", "UP")
            "上下" -> arrayListOf("UP", "DOWN", "LEFT", "RIGHT")
            "左右" -> arrayListOf("LEFT", "RIGHT", "UP", "DOWN")
            else -> arrayListOf("UP", "DOWN", "LEFT", "RIGHT")
        }
    }
    private fun getDirectionImage(item:Pokemon,pokemon:Pokemon): Int{
        Log.d("pokemon direction",item.direction.toString())
        return when(item.direction){
            "D214" -> R.drawable.d214
            "D236" -> R.drawable.d236
            "D258" -> R.drawable.d258
            "D456" -> R.drawable.d456
            "D478" -> R.drawable.d478
            "D698" -> R.drawable.d698
            else -> getPokemonImage(pokemon)
        }
    }

    private fun getDirectionNumber(
        oldDirection: String,
        newDirection: String,
        directionChanged: Boolean
    ): String? {
        if (!directionChanged) { //方向沒改
            if ((oldDirection == "RIGHT" || oldDirection == "LEFT") && (newDirection == "LEFT" || newDirection == "RIGHT")) {
                return "D456"
            } else if ((oldDirection == "UP" || oldDirection == "DOWN") && (newDirection == "DOWN" || newDirection == "UP")) {
                return "D258"
            } else {
                return "head"
            }
        } else { //方向變更
            if ((oldDirection == "RIGHT" && newDirection == "UP") || (oldDirection == "DOWN" && newDirection == "LEFT")) {
                return "D236"
            }//右to上 下to左
            else if ((oldDirection == "UP" && newDirection == "LEFT") || (oldDirection == "RIGHT" && newDirection == "DOWN")) {
                return "D698"
            }//上to左 右to下
            else if ((oldDirection == "UP" && newDirection == "RIGHT") || (oldDirection == "LEFT" && newDirection == "DOWN")) {
                return "D478"
            }//上to右 左to下
            else if ((oldDirection == "DOWN" && newDirection == "RIGHT") || (oldDirection == "LEFT" && newDirection == "UP")) {
                return "D214"
            }//左to上 //下to右

        }
        return null
    }
    private fun playSound(sound:String){
        player.reset()
        player = MediaPlayer.create(this,when(sound){
            "hit" -> R.raw.hit
            "cancel" -> R.raw.cancel
            "spell" -> R.raw.spell
            "select" -> R.raw.select
            "victory" -> R.raw.victory
            "open" -> R.raw.pokeball_opening
            "ohoh" -> R.raw.ohoh
            "miss" -> R.raw.miss
            "clear" -> R.raw.clear
            "gameover" -> R.raw.gameover
            "press" -> R.raw.press
            else -> R.raw.cancel
        })
        player.start()
    }
    class Pokemon{
        var name: String? = null
        var x: Int? = null
        var y: Int? = null
        var direction: String? = null
    }
}
