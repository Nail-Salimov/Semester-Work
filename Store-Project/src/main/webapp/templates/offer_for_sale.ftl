<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>

    <script>
        function handleFileSelect(evt) {
            var file = evt.target.files; // FileList object
            // Loop through the FileList and render image files as thumbnails.
            for (var i = 0, f; f = files[i]; i++) {
                // Only process image files.
                if (!f.type.match('image.*')) {
                    alert("Image only please....");
                }
                var reader = new FileReader();
                // Closure to capture the file information.
                reader.onload = (function (theFile) {
                    return function (e) {
                        // Render thumbnail.
                        var span = document.createElement('span');
                        span.innerHTML = ['<img class="thumb" title="', escape(theFile.name), '" src="', e.target.result, '" />'].join('');
                        document.getElementById('output').insertBefore(span, null);
                    };
                })(f);
                // Read in the image file as a data URL.
                reader.readAsDataURL(f);
            }
        }
        document.getElementById('file').addEventListener('change', handleFileSelect, false);
    </script>

    <script type="text/javascript">
        function add() {
            $("#file").clone().appendTo(".container");
        }
    </script>
    <title>Document</title>

</head>
<body>
<nav>
    <a href="/store">Store</a>
</nav>
<div>
    <#if error?has_content>
        <p>${error}</p>
    </#if>
</div>


<div>
    <form enctype="multipart/form-data" action="/offer_for_sale?${_csrf.parameterName}=${_csrf.token}" method="post">

        <input type="file" id="file" accept="image/*" name="image" placeholder="Имя файла..."/>

        <div class="container">
            <button type="button" onclick="add()">Добавить изображение</button>
        </div>

        <div>
            <p>Название товара: <input type="text" name="name" placeholder="Name"/></p>
            <p>Описание товара: <input type="text" name="description" placeholder="description"/></p>
            <p> Начальная цена: <input type="number" name="maxCost" placeholder="maxCost"/></p>
            <p>Наименьшая цена: <input type="number" name="minCost" placeholder="minCost"/></p>
            <p> Количество: <input type="number" name="count" placeholder="count"/></p>
            <p>Уменьшение цены через час на: <input type="number" name="decrease" placeholder="decrease"/></p>

            <button type="submit">Add</button>
        </div>
    </form>

</div>
</body>
</html>