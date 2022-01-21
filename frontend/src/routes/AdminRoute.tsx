import { HashRouter, Route, Routes } from 'react-router-dom';
import AdminLayout from '../layouts/AdminLayout';
import ManageQaSample from '../App/ManageDataSample/ManageQaSample';
import CreateData from '../App/CreateDataSample/CreateData';
import QaSampleDetailView from '../App/ManageDataSample/QaSampleDetailView';
import Welcome from '../App/Welcome';

const AdminRoute = () => (
    <HashRouter>
        <Routes>
            <Route path="/" element={<AdminLayout />}>
                <Route path="/" element={<Welcome />} />
                <Route path="qa-sample" element={<ManageQaSample />} />
                <Route path="qa-sample/:qaSampleId" element={<QaSampleDetailView />} />
                <Route path="create" element={<CreateData />} />
            </Route>
        </Routes>
    </HashRouter>
);

export default AdminRoute;
