document.addEventListener('DOMContentLoaded', () => {
  const form = document.querySelector('.js-form');
  const linkId = location.pathname.match('\/link\/(.+)\/?')[1];
  const userId = location.pathname.match('\/dashboard\/([a-z0-9A-Z-]+)\/')[1];

  form.addEventListener('submit', (e) => {
    e.preventDefault();

    const formData = new FormData(e.target);
    const href = formData.get('url');
    const r_limit = formData.get('r_limit');

    updateLink(linkId, href, r_limit)
        .then((data) => {
            location = `/dashboard/${userId}`;
        })
        .catch(() => {
            e.target.classList.add('error')
        })
        .finally(() => {
            e.target.setAttribute('disabled', false);
        })
  })
})