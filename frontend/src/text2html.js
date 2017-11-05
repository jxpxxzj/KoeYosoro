/*
    character: 'w'
    right: 'true'
*/

const correct = "#4caf50"
const wrong = "#f44336"

function parseResult(result, fontSize = "14px", fontWeight = "regular", joinText='') {
    var allCorrect = true;
    var original = [];
    var htmls = result.map(t => {
        console.log(t);
        console.log(t.right);
        if (t.right === false) 
            allCorrect = false;
        original.push(t.character);
        return `<span style="color: ${t.right === true ? correct : wrong}; font-size:${fontSize}; font-weight:${fontWeight}">${t.character}</span>`
    });

    console.log(htmls.join(joinText));

    return {
        html: htmls.join(joinText),
        original,
        allCorrect
    };
}

module.exports = parseResult;