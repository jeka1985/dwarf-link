
document.addEventListener('DOMContentLoaded', () => {
  const root = document.querySelector('.js-root');

  root.addEventListener('click', (e) => {
      const target = e.target;

      if (target.matches('[data-action="remove"]')) {
        const id = target.getAttribute('data-id');

        deleteLink(id).
            then(({id}) => {
                document.querySelector(`.listing__row[data-id="${id}"]`)?.remove();
            })
      }
  })
})