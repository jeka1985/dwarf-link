async function createLink(href, day_limits) {
    const response = await fetch('/bff/links/', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({href, day_limits})
    });

    if (response.status !== 200) {
        throw new Error('API Error')
    }

    return await response.json();
}

async function updateLink(id, href, r_limit) {
    const response = await fetch(`/bff/links/${id}`, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({href, r_limit})
    });

    if (response.status !== 200) {
        throw new Error('API Error')
    }

    return await response.json();
}

async function deleteLink(id) {
    const response = await fetch('/bff/links/' + id, {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
    });

    if (response.status !== 200) {
        throw new Error('API Error')
    }

    return await response.json();
}

