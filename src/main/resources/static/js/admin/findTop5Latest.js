
    function fetchProducts() {
    fetch('/admin/latest-products')
        .then(response => response.json())
        .then(data => {
            const toolboxList = document.querySelector('.title1');
            toolboxList.innerHTML = '';

            data.forEach(product => {
                const listItem = `

                    <ul class="list-unstyled timeline widget">
                        <div class="block">
                            <div class="block_content">
                                <h2 class="title">
                                    <a href="#">${product.productName}</a>
                                </h2>
                                <div class="byline">
                                    <span>${new Date(product.createdAt).toLocaleString()}</span> by <a>${product.member.memberName}</a>
                                </div>
                                <p class="excerpt">${product.description}</p>
                            </div>
                        </div>

</ul>
</div>
</div>`;


                toolboxList.innerHTML += listItem;
            });
        });
}

    setInterval(fetchProducts, 5000);
