const menu = new MmenuLight(
    document.querySelector('#menu-mobile')
);

const navigator = menu.navigation({
    selectedClass: 'selected',
    slidingSubmenus: true,
    title: 'Menu'
});

const drawer = menu.offcanvas();

document
    .querySelector('a[href="#menu-mobile"]')
    .addEventListener('click', (ev) => {
        ev.preventDefault();
        drawer.open();
    });
