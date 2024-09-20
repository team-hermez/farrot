function fetchProducts() {
    fetch('/admin/latest-products')
        .then(response => response.json())
        .then(data => {
            const toolboxList = document.querySelector('.title1');
            toolboxList.innerHTML = ''; // 기존 내용 초기화

            data.forEach(product => {
                const listItem = `
                    <ul class="list-unstyled timeline widget">
                        <li>
                            <div class="block">
                                <div class="block_content">
                                    <h2 class="title">
                                        <a href="/product/product-detail?productId=${product.id}">
                                            ${product.productName}
                                        </a>
                                    </h2>
                                    <div class="byline">
                                        <span>${new Date(product.createdAt).toLocaleString()}</span> by <a>${product.member.memberName}</a>
                                    </div>
                                    <p class="excerpt">${product.description}</p>
                                </div>
                            </div>
                        </li>
                    </ul>`;

                toolboxList.innerHTML += listItem;
            });
        })
        .catch(error => console.error('Error fetching products:', error));
}

setInterval(fetchProducts, 5000);
