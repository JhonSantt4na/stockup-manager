// Permissions.jsx
import React, { useState, useEffect, useCallback } from 'react';
import './Permissions.css';
import PermissionService from '../../Services/PermissionsService';
import AddPermissionModal from '../../components/Modals/Permissions/AddPermissionModal';
import EditPermissionModal from '../../components/Modals/Permissions/EditPermissionModal';
import DeleteConfirmModal from '../../components/Modals/Permissions/DeleteConfirmModal';

const Permissions = () => {
  const [permissions, setPermissions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [searchTerm, setSearchTerm] = useState('');
  const [sort, setSort] = useState(['name', 'asc']);
  const [showAddModal, setShowAddModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [selectedPermission, setSelectedPermission] = useState(null);
  const [showActiveOnly, setShowActiveOnly] = useState(true);
  const [expandedRows, setExpandedRows] = useState(new Set());

  const pageSize = 10;

  const fetchPermissions = useCallback(async () => {
    setLoading(true);
    try {
      const response = showActiveOnly
        ? await PermissionService.getAllActivePermissions(currentPage, pageSize, sort)
        : await PermissionService.getAllPermissions(currentPage, pageSize, sort);
      setPermissions(response.content || []);
      setTotalPages(response.totalPages || 0);
    } catch (err) {
      setError('Failed to load permissions');
    } finally {
      setLoading(false);
    }
  }, [currentPage, sort, showActiveOnly]);

  useEffect(() => {
    fetchPermissions();
  }, [fetchPermissions]);

  const handleSearch = (e) => {
    setSearchTerm(e.target.value);
  };

  const filteredPermissions = permissions.filter(perm =>
    perm.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    perm.description.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleSort = (column) => {
    setCurrentPage(0);
    setSort(prev => {
      const newDir = prev[0] === column && prev[1] === 'asc' ? 'desc' : 'asc';
      return [column, newDir];
    });
  };

  const handleAddPermission = async (data) => {
    try {
      await PermissionService.createPermission(data);
      setShowAddModal(false);
      fetchPermissions();
    } catch (err) {
      console.error('Error adding permission', err);
    }
  };

  const handleEditPermission = async (data) => {
    try {
      await PermissionService.updatePermission(data);
      setShowEditModal(false);
      fetchPermissions();
    } catch (err) {
      console.error('Error updating permission', err);
    }
  };

  const handleDeletePermission = async () => {
    try {
      await PermissionService.deletePermission(selectedPermission.name);
      setShowDeleteModal(false);
      fetchPermissions();
    } catch (err) {
      console.error('Error deleting permission', err);
    }
  };

  const openEditModal = (perm) => {
    setSelectedPermission(perm);
    setShowEditModal(true);
  };

  const openDeleteModal = (perm) => {
    setSelectedPermission(perm);
    setShowDeleteModal(true);
  };

  const toggleActiveOnly = () => {
    setShowActiveOnly(!showActiveOnly);
    setCurrentPage(0);
  };

  const toggleExpandRow = (id) => {
    setExpandedRows(prev => {
      const newSet = new Set(prev);
      if (newSet.has(id)) {
        newSet.delete(id);
      } else {
        newSet.add(id);
      }
      return newSet;
    });
  };

  const nextPage = () => {
    if (currentPage < totalPages - 1) setCurrentPage(currentPage + 1);
  };

  const prevPage = () => {
    if (currentPage > 0) setCurrentPage(currentPage - 1);
  };

  if (loading) return <div className="loading">Loading...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="permissions-container">
      <div className="permissions-header">
        <h2>Permissions Management</h2>
      </div>
      <div className="header-controls">
        <div className="controls-group">
          <input
            type="text"
            placeholder="Search permissions..."
            value={searchTerm}
            onChange={handleSearch}
            className="search-box"
          />
          <button onClick={toggleActiveOnly} className="btn-add">
            {showActiveOnly ? 'Show All' : 'Show Active Only'}
          </button>
        </div>
        <button onClick={() => setShowAddModal(true)} className="btn-add">
          Add Permission
        </button>
      </div>
      <div className="table-container">
        <table className="permissions-table">
          <thead>
            <tr>
              <th onClick={() => handleSort('name')} className="sortable">Name</th>
              <th onClick={() => handleSort('description')} className="sortable">Description</th>
              <th onClick={() => handleSort('enabled')} className="sortable">Status</th>
              <th>Roles</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {filteredPermissions.map(perm => (
              <tr key={perm.id}>
                <td>{perm.name}</td>
                <td>{perm.description}</td>
                <td>{perm.enabled ? 'Active' : 'Inactive'}</td>
                <td className="roles-column">
                  <div className="roles-wrapper">
                    {perm.roles.map((role, index) => {
                      if (!expandedRows.has(perm.id) && index >= 3) return null;
                      return (
                        <span key={role.id} className="role-tag">
                          {role.name}
                        </span>
                      );
                    })}
                    {perm.roles.length > 3 && (
                      <button
                        type="button"
                        className="ver-mais-link"
                        onClick={() => toggleExpandRow(perm.id)}
                      >
                        {expandedRows.has(perm.id) ? 'Show less' : `+${perm.roles.length - 3} more`}
                      </button>
                    )}
                  </div>
                </td>
                <td>
                  <div className="actions-inline">
                    <button onClick={() => openEditModal(perm)} className="btn-manage">
                      Edit
                    </button>
                    <button onClick={() => openDeleteModal(perm)} className="btn-delete">
                      Delete
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div className="pagination">
        <button onClick={prevPage} disabled={currentPage === 0}>Previous</button>
        <span>Page {currentPage + 1} of {totalPages}</span>
        <button onClick={nextPage} disabled={currentPage === totalPages - 1}>Next</button>
      </div>

      {showAddModal && (
        <AddPermissionModal
          onClose={() => setShowAddModal(false)}
          onSubmit={handleAddPermission}
        />
      )}
      {showEditModal && selectedPermission && (
        <EditPermissionModal
          permission={selectedPermission}
          onClose={() => setShowEditModal(false)}
          onSubmit={handleEditPermission}
        />
      )}
      {showDeleteModal && selectedPermission && (
        <DeleteConfirmModal
          itemName={selectedPermission.name}
          onClose={() => setShowDeleteModal(false)}
          onConfirm={handleDeletePermission}
        />
      )}
    </div>
  );
};

export default Permissions;