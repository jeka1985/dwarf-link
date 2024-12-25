
function resetResults(wrapper) {
    wrapper.innerHTML = "";
}

function renderResultLink(wrapper, href) {
    const link = document.createElement('a');

    link.classList.add('wizard__result-link');
    link.setAttribute('href', href);
    link.innerText = href;

    wrapper.appendChild(link);
}

document.addEventListener('DOMContentLoaded', () => {
  const form = document.querySelector('.js-form');
  const result = document.querySelector('.js-result');

  form.addEventListener('reset', (e) => {
    this.resetResults(result);
  })

  form.addEventListener('submit', (e) => {
    e.preventDefault();

    const formData = new FormData(e.target);
    const href = formData.get('url');
    const day_limits = formData.get('day_limits');

    e.target.setAttribute('disabled', true);
    e.target.classList.remove('error');
    result.innerHTML = "";


    console.log({href, day_limits})

    createLink(href, day_limits)
        .then((data) => {
            renderResultLink(result, `${location.origin}/r/${data.uid}`)
        })
        .catch((e) => {
            debugger;
            e.target.classList.add('error')
        })
        .finally(() => {
            e.target.setAttribute('disabled', false);
        })
  })
})