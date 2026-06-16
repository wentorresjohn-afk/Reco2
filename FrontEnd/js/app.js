const API = "https://reco2-5b98.onrender.com";

/* =========================
   TOAST SYSTEM (Sincronizado)
   ========================= */
function showToast(message, type = "success") {
    const toast = document.createElement("div");
    toast.className = `alert alert-${type} position-fixed`;
    toast.style.bottom = "20px";
    toast.style.right = "20px";
    toast.style.zIndex = "1055";
    toast.innerHTML = message;

    document.body.appendChild(toast);

    setTimeout(() => {
        toast.remove();
    }, 3800);
}

/* =========================
   FORMATO FECHA
   ========================= */
function formatDate(date) {
    if (!date) return "";
    const d = new Date(date);
    const day = String(d.getDate()).padStart(2, '0');
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const year = d.getFullYear();
    const hour = String(d.getHours()).padStart(2, '0');
    const minute = String(d.getMinutes()).padStart(2, '0');

    return `${day}-${month}-${year} ${hour}:${minute}`;
}

/* =========================
   DASHBOARD
   ========================= */
async function loadDashboard() {
    try {
        const users = await fetch(`${API}/users/all`).then(r => r.json());
        const spaces = await fetch(`${API}/spaces/all`).then(r => r.json());
        const reservations = await fetch(`${API}/reservation/all`).then(r => r.json());

        const totalUsers = document.getElementById("totalUsers");
        const totalSpaces = document.getElementById("totalSpaces");
        const totalReservations = document.getElementById("totalReservations");

        if (totalUsers) totalUsers.textContent = users.length;
        if (totalSpaces) totalSpaces.textContent = spaces.length;
        if (totalReservations) totalReservations.textContent = reservations.length;

    } catch (error) {
        console.error(error);
        showToast("Error cargando dashboard", "danger");
    }
}

/* =========================
   USUARIOS (CRUD Completado)
   ========================= */
async function loadUsers() {
    const tbody = document.getElementById("tbodyUsers");
    if (!tbody) return; 

    try {
        const response = await fetch(`${API}/users/all`);
        if (response.ok) {
            const users = await response.json(); // Se lee correctamente como JSON
            tbody.innerHTML = ""; 

            users.forEach(user => {
                tbody.innerHTML += `
                    <tr>
                        <td class="ps-3">${user.id}</td>
                        <td>${user.name}</td>
                        <td>${user.email}</td>
                        <td><span class="badge bg-secondary">${user.role}</span></td>
                        <td class="text-end pe-3">
                            <button class="btn btn-sm btn-outline-warning me-1" onclick="openEditUser(${user.id}, '${user.name}', '${user.email}', '${user.role}')">
                                <i class="bi bi-pencil-square"></i>
                            </button>
                            <button class="btn btn-sm btn-outline-danger" onclick="deleteUser(${user.id})">
                                <i class="bi bi-trash-fill"></i>
                            </button>
                        </td>
                    </tr>
                `;
            });
        } else {
            console.error("Error al obtener usuarios de la API");
        }
    } catch (error) {
        console.error("Error en la petición fetch de usuarios:", error);
    }
}

async function registerUser() {
    // Sincronizado con los IDs exactos de usuarios.html
    const user = {
        name: document.getElementById("userName").value,
        email: document.getElementById("userEmail").value,
        password: document.getElementById("userPassword").value,
        role: document.getElementById("userRole").value
    };

    try {
        const response = await fetch(`${API}/users/add`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(user)
        });

        const result = await response.text();

        if (response.ok) {
            showToast(result || "Usuario registrado");
            loadUsers();
            
            // Limpiar campos
            document.getElementById("userName").value = "";
            document.getElementById("userEmail").value = "";
            document.getElementById("userPassword").value = "";
            document.getElementById("userRole").value = "user";

            // Cerrar Modal automáticamente
            const modal = bootstrap.Modal.getInstance(document.getElementById('userModal'));
            if(modal) modal.hide();
        } else {
            showToast(result, "danger");
        }
    } catch (error) {
        console.error(error);
        showToast("Error registrando usuario", "danger");
    }
}

/* =========================
   ESPACIOS
   ========================= */
async function loadSpaces() {
    const container = document.getElementById("spacesContainer");
    if (!container) return;

    try {
        const response = await fetch(`${API}/spaces/all`);
        const spaces = await response.json();
        container.innerHTML = "";

        spaces.forEach(space => {
            container.innerHTML += `
            <div class="col-lg-4 col-md-6 mb-4 space-card-item">
                <div class="card h-100 shadow-sm">
                    <div class="card-header bg-light d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">${space.name}</h5>
                        <div>
                            <button class="btn btn-sm btn-outline-warning me-1" onclick="openEditSpace(${space.id}, '${space.name}', '${space.location}', '${space.type}', ${space.price})">
                                <i class="bi bi-pencil-square"></i>
                            </button>
                            <button class="btn btn-sm btn-outline-danger" onclick="deleteSpace(${space.id})">
                                <i class="bi bi-trash-fill"></i>
                            </button>
                        </div>
                    </div>
                    <div class="card-body">
                        <p class="mb-1">📍 <strong>Ubicación:</strong> ${space.location}</p>
                        <p class="mb-1">🏷️ <strong>Tipo:</strong> ${space.type}</p>
                        <h5 class="text-success mt-3 mb-0">₡${space.price}</h5>
                    </div>
                </div>
            </div>
            `;
        });
    } catch (error) {
        console.error(error);
        showToast("Error cargando espacios", "danger");
    }
}

async function registerSpace() {
    const space = {
        name: document.getElementById("spaceName").value,
        location: document.getElementById("location").value,
        type: document.getElementById("type").value,
        price: parseFloat(document.getElementById("price").value)
    };

    try {
        const response = await fetch(`${API}/spaces/add`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(space)
        });

        const result = await response.text();

        if (response.ok) {
            showToast(result || "Espacio creado");
            loadSpaces();

            document.getElementById("spaceName").value = "";
            document.getElementById("location").value = "";
            document.getElementById("type").value = "";
            document.getElementById("price").value = "";

            const modal = bootstrap.Modal.getInstance(document.getElementById('spaceModal'));
            if(modal) modal.hide();
        } else {
            showToast(result, "danger");
        }
    } catch (error) {
        console.error(error);
        showToast("Error registrando espacio", "danger");
    }
}

/* =========================
   RESERVACIONES
   ========================= */
async function loadReservations() {
    const tbody = document.getElementById("reservationTable");
    if (!tbody) return;

    try {
        const response = await fetch(`${API}/reservation/all`);
        const reservations = await response.json();
        tbody.innerHTML = "";

        reservations.forEach(r => {
            let badge = `<span class="badge bg-warning">${r.status}</span>`;
            if (r.status === "CONFIRMED") badge = `<span class="badge bg-success">${r.status}</span>`;
            if (r.status === "CANCELED") badge = `<span class="badge bg-danger">${r.status}</span>`;

            tbody.innerHTML += `
                <tr>
                    <td>${r.id}</td>
                    <td>${r.space?.name || "N/A"}</td>
                    <td>${r.user?.email || "N/A"}</td>
                    <td>${r.startDay || ""}</td>
                    <td>${r.endDay || ""}</td>
                    <td>${badge}</td>
                </tr>
            `;
        });
    } catch (error) {
        console.error(error);
        showToast("Error cargando reservaciones", "danger");
    }
}

async function registerReservation() {
    const reservation = {
        spaceId: parseInt(document.getElementById("spaceId").value),
        userEmail: document.getElementById("userEmail").value,
        startDay: formatDate(document.getElementById("startDay").value),
        endDay: formatDate(document.getElementById("endDay").value),
        status: document.getElementById("status").value
    };

    try {
        const response = await fetch(`${API}/reservation/add`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(reservation)
        });

        const result = await response.text();

        if (response.ok) {
            showToast(result || "Reservación creada");
            loadReservations();

            document.getElementById("spaceId").value = "";
            document.getElementById("userEmail").value = "";
            document.getElementById("startDay").value = "";
            document.getElementById("endDay").value = "";
            document.getElementById("status").value = "PENDING";

            const modal = bootstrap.Modal.getInstance(document.getElementById('reservationModal'));
            if(modal) modal.hide();
        } else {
            showToast(result, "danger");
        }
    } catch (error) {
        console.error(error);
        showToast("Error registrando reservación", "danger");
    }
}

/* =========================================
   FUNCIONES AVANZADAS DE USUARIOS (CRUD)
   ========================================= */
async function deleteUser(id) {
    if (!confirm("¿Estás completamente seguro de que deseas eliminar este usuario?")) return;

    try {
        const response = await fetch(`${API}/users/delete/${id}`, {
            method: "DELETE"
        });
        const result = await response.text();

        if (response.ok) {
            showToast(result || "Usuario eliminado con éxito");
            loadUsers(); 
            if (document.getElementById("totalUsers")) loadDashboard(); 
        } else {
            showToast(result, "danger");
        }
    } catch (error) {
        console.error(error);
        showToast("Error al intentar eliminar el usuario", "danger");
    }
}

function searchUsers() {
    const input = document.getElementById("searchUser").value.toLowerCase();
    const table = document.getElementById("tbodyUsers");
    if (!table) return;
    const rows = table.getElementsByTagName("tr");

    for (let i = 0; i < rows.length; i++) {
        const nameColumn = rows[i].getElementsByTagName("td")[1];
        const emailColumn = rows[i].getElementsByTagName("td")[2];
        
        if (nameColumn || emailColumn) {
            const nameText = nameColumn.textContent.toLowerCase();
            const emailText = emailColumn.textContent.toLowerCase();
            
            if (nameText.includes(input) || emailText.includes(input)) {
                rows[i].style.display = "";
            } else {
                rows[i].style.display = "none";
            }
        }
    }
}

function openEditUser(id, name, email, role) {
    document.getElementById("editUserId").value = id;
    document.getElementById("editName").value = name;
    document.getElementById("editEmail").value = email;
    document.getElementById("editRole").value = role;

    const editModal = new bootstrap.Modal(document.getElementById('editUserModal'));
    editModal.show();
}

async function updateUser() {
    const id = document.getElementById("editUserId").value;
    const updatedUser = {
        name: document.getElementById("editName").value,
        email: document.getElementById("editEmail").value,
        role: document.getElementById("editRole").value
    };

    try {
        const response = await fetch(`${API}/users/update/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(updatedUser)
        });
        const result = await response.text();

        if (response.ok) {
            showToast("Usuario actualizado correctamente");
            loadUsers(); 

            const modalElement = document.getElementById('editUserModal');
            const modal = bootstrap.Modal.getInstance(modalElement);
            if (modal) modal.hide();
        } else {
            showToast(result, "danger");
        }
    } catch (error) {
        console.error(error);
        showToast("Error al actualizar usuario", "danger");
    }
}

/* =========================================
   FUNCIONES AVANZADAS DE ESPACIOS (CRUD)
   ========================================= */
async function deleteSpace(id) {
    if (!confirm("¿Deseas eliminar este espacio permanentemente?")) return;

    try {
        const response = await fetch(`${API}/spaces/delete/${id}`, {
            method: "DELETE"
        });
        const result = await response.text();

        if (response.ok) {
            showToast(result || "Espacio eliminado con éxito");
            loadSpaces(); 
            if (document.getElementById("totalSpaces")) loadDashboard(); 
        } else {
            showToast(result, "danger");
        }
    } catch (error) {
        console.error(error);
        showToast("Error al intentar borrar el espacio", "danger");
    }
}

function searchSpaces() {
    const input = document.getElementById("searchSpace").value.toLowerCase();
    const cards = document.getElementsByClassName("space-card-item");

    for (let i = 0; i < cards.length; i++) {
        const cardText = cards[i].textContent.toLowerCase();
        if (cardText.includes(input)) {
            cards[i].style.display = "";
        } else {
            cards[i].style.display = "none";
        }
    }
}

function openEditSpace(id, name, location, type, price) {
    document.getElementById("editSpaceId").value = id;
    document.getElementById("editSpaceName").value = name;
    document.getElementById("editLocation").value = location;
    document.getElementById("editType").value = type;
    document.getElementById("editPrice").value = price;

    const editModal = new bootstrap.Modal(document.getElementById('editSpaceModal'));
    editModal.show();
}

async function updateSpace() {
    const id = document.getElementById("editSpaceId").value;
    const updatedSpace = {
        spaceName: document.getElementById("editSpaceName").value,
        location: document.getElementById("editLocation").value,
        type: document.getElementById("editType").value,
        price: parseFloat(document.getElementById("editPrice").value)
    };

    try {
        const response = await fetch(`${API}/spaces/update/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(updatedSpace)
        });

        if (response.ok) {
            showToast("Espacio modificado con éxito");
            loadSpaces(); 

            const modalElement = document.getElementById('editSpaceModal');
            const modal = bootstrap.Modal.getInstance(modalElement);
            if (modal) modal.hide();
        } else {
            const errorText = await response.text();
            showToast(errorText, "danger");
        }
    } catch (error) {
        console.error(error);
        showToast("Error de conexión al actualizar espacio", "danger");
    }
}

/* =========================
   ENRUTADOR INTELIGENTE DE CARGA
   ========================= */
document.addEventListener("DOMContentLoaded", () => {
    if (document.getElementById("totalUsers")) loadDashboard();
    if (document.getElementById("tbodyUsers")) loadUsers();
    if (document.getElementById("spacesContainer")) loadSpaces();
    if (document.getElementById("reservationTable")) loadReservations();
});