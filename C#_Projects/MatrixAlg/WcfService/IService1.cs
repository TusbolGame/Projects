using MathNet.Numerics.LinearAlgebra;
using MathNet.Numerics.LinearAlgebra.Double;
using MathNet.Numerics.LinearAlgebra.Storage;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;


namespace WcfService
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IService1" in both code and config file together.
    [ServiceContract]
    public interface IService1
    {
        [OperationContract]
        string SetCalcInstance(MyAlgorithm algorithm);
        [OperationContract]
        string CreateCalcInstance(int product, int player, int necessity);

        [OperationContract]
        void CalculateResult(int index);

        [OperationContract]
        int GetStatus();

        [OperationContract]
        string GetRestrictionString();

        [OperationContract]
        string GetMaxString();

        [OperationContract]
        string GetMaxValue();

        [OperationContract]
        double GetResult();

        [OperationContract]
        void AssignMatrix(string lines, int type);
        
    }

    [DataContract]
    public class RowCol
    {
        [DataMember]
        public int _row { get; set; }
        [DataMember]
        public int _col { get; set; }
        public RowCol() { _row = 0; _col = 0; }
    }

    [Serializable]
    [DataContract]
    public partial class MyAlgorithm
    {
        [DataMember]
        public int _product { get; set; }
        [DataMember]
        public int _player { get; set; }
        [DataMember]
        public int _necessity { get; set; }

        [DataMember]
        public int _np { get; set; }
        [DataMember]
        public int _m { get; set; }
        [DataMember]
        public int _br { get; set; }

        [DataMember]
        public bool _assigned_contract { get; set; }
        [DataMember]
        public bool _assigned_A { get; set; }
        [DataMember]
        public bool _assigned_B { get; set; }
        [DataMember]
        public bool _assigned_C { get; set; }
        [DataMember]
        public bool _assigned_D { get; set; }
        [DataMember]
        public bool _assigned_P { get; set; }

        //[DataMember]
        public Matrix<double> _contract { get; set; }                   // contract matrix -> dimension : np * m
        //[DataMember]
        public Matrix<double> _A { get; set; }                          // resource matrix Ari -> dimension : np * np   (necessity equals to Product [np])
        //[DataMember]
        public Matrix<double> _B { get; set; }                          // resource matrix B -> dimension : m * np
        //[DataMember]
        public Matrix<double> _C { get; set; }                          // Cost matrix (exactly vector) -> dimension : 1 * np
        //[DataMember]
        public Matrix<double> _D { get; set; }                          // Distribution matrix -> dimension : 2 * np   (mu, sigma)
        //[DataMember]
        public Matrix<double> _P { get; set; }                          // Sales matrix (exactly vector) -> dimension : 1 * np
        //[DataMember]
        public Matrix<double> _binary { get; set; }                        // binary matrix -> dimension : m * (2^m - 1)
        [DataMember]
        public static List<string> _binary_tmp { get; set; }                    // binary matrix

        public MyAlgorithm()
        {
            _product = 0;
            _product = 0;
            _necessity = 0;
            _np = 0;
            _m = 0;
            _br = 0;

            _assigned_contract = false;
            _assigned_A = false;
            _assigned_B = false;
            _assigned_C = false;
            _assigned_D = false;
            _assigned_P = false;
        }

        public MyAlgorithm(int product, int player, int necessity)
        {
            _product = product;
            _player = player;
            _necessity = necessity;

            if (_contract != null) _contract.Clear();
            if (_A != null) _A.Clear();
            if (_B != null) _B.Clear();
            if (_C != null) _C.Clear();
            if (_D != null) _D.Clear();
            if (_P != null) _P.Clear();
            if (_binary != null) _binary.Clear();

            _m = (int)(Math.Pow(2, _player) - 1);
            _np = _product;
            _br = (int)(Math.Pow(2, _m) - 1);

            // generate binary
            _binary_tmp = new List<string>();
            string binaryLength = new string('?', _m);
            generateBinary(binaryLength.ToCharArray(), 0);

            // set dimension to the matrix
            _contract = Matrix<double>.Build.Dense(_np, _m);
            _A = Matrix<double>.Build.Dense(_np, _np);
            _B = Matrix<double>.Build.Dense(_np, _m);
            _C = Matrix<double>.Build.Dense(_np, 1);
            _D = Matrix<double>.Build.Dense(_np, 2);
            _P = Matrix<double>.Build.Dense(_np, 1);
            _binary = Matrix<double>.Build.Dense(_br, _m);


            _binary_tmp.RemoveAt(_br);
            int i = 0;
            foreach (var item in _binary_tmp)
            {
                for (int j = 0; j < item.Length; j++)
                {
                    double d = item[j] - '0';
                    _binary[i, j] = d;
                }
                i++;
            }
        }

        public string getAssignedData(int type)
        {
            if (type == 0) return _contract.ToString();
            else if (type == 1) return _A.ToString();
            else if (type == 2) return _B.ToString();
            else if (type == 3) return _C.ToString();
            else if (type == 4) return _D.ToString();
            else if (type == 5) return _P.ToString();
            else if (type == 6) return _binary.ToString();

            return "";
        }

        public void Reset(int type)
        {
            if (type == -1)
            {
                _contract.Clear();
                _A.Clear();
                _B.Clear();
                _C.Clear();
                _D.Clear();
                _P.Clear();
                _binary.Clear();
            }
            else if (type == 0)
                _contract.Clear();
            else if (type == 1)
                _A.Clear();
            else if (type == 2)
                _B.Clear();
            else if (type == 3)
                _C.Clear();
            else if (type == 4)
                _D.Clear();
            else if (type == 5)
                _P.Clear();
            else if (type == 6)
                _binary.Clear();
        }

        public string Assign(string lines, int type)
        {
            string result = "";

            RowCol v = getRowColCount(lines);
            if (v._col == -1)
                return "Column is illegal, check again please";
            if (v._row == 0 || v._col == 0)
                return "No data";
            RowCol v1 = new RowCol();
            if (type == 0 || type == 2)
            {
                v1 = new RowCol() { _row = _np, _col = _m };
            }
            else if (type == 1)
            {
                v1 = new RowCol { _row = _np, _col = _necessity };
            }
            else if (type == 3 || type == 5)
            {
                v1 = new RowCol { _row = _np, _col = 1 };
            }
            else if (type == 4)
            {
                v1 = new RowCol { _row = 2, _col = _np };
            }
            else if (type == 6)
            {
                v1 = new RowCol { _row = _br, _col = _m };
            }
            string msg = CheckMatch(v, v1);
            if (msg != "Success")
                return msg;

            // Assign data
            if (!AssignData(lines, type))
                result = "Something went wrong while assign data";
            else
                result = "Success";
            return result;
        }

        public bool AssignData(string lines, int type)
        {
            int i = 0;
            foreach (var row in lines.Split(new string[] { Environment.NewLine }, StringSplitOptions.None))
            {
                int j = 0;
                foreach (var col in row.Trim().Split(' '))
                {
                    double c = 0;
                    if (!double.TryParse(col, out c))
                    {
                        return false;
                    }

                    if (type == 0) _contract[i, j] = c;
                    else if (type == 1) _A[i, j] = c;
                    else if (type == 2) _B[i, j] = c;
                    else if (type == 3) _C[i, j] = c;
                    else if (type == 4) _D[i, j] = c;
                    else if (type == 5) _P[i, j] = c;

                    j++;
                }
                i++;
            }

            if (type == 0) _assigned_contract = true;
            else if (type == 1) _assigned_A = true;
            else if (type == 2) _assigned_B = true;
            else if (type == 3) _assigned_C = true;
            else if (type == 4) _assigned_D = true;
            else if (type == 5) _assigned_P = true;
            return true;
        }

        public RowCol getRowColCount(string lines)
        {
            RowCol res = new RowCol();
            int cols = 0;

            foreach (var row in lines.Split(new string[] { Environment.NewLine }, StringSplitOptions.None))
            {
                if (row != "\r")
                    res._row++;
                var col = row.Trim().Split(' ');
                if (col == null && col.Count() == 0)
                    continue;

                if (cols == 0)
                {
                    cols = col.Count();
                }
                else if (col.Count() != cols)
                {
                    res._col = -1;
                    return res;
                }
            }
            res._col = cols;

            return res;
        }

        public string CheckMatch(RowCol arg1, RowCol arg2)
        {
            if (arg1._row != arg2._row)
                return "Row count is not correct\nRow count should be " + arg2._row.ToString();
            if (arg1._col != arg2._col)
                return "Column count is not correct\nColumn count should be " + arg2._col.ToString();
            return "Success";
        }

        public List<string> getResult()
        {
            /*
            List<string> res = new List<string>();
            for(int k = 0; k < _matrix_binary.Count(); k++)
            {
                for (int i = 0; i < _np; i++)
                {
                    string left = "";
                    string right = "";
                    for (int j = 0; j < _necessity; j++)
                    {
                        left += _matrix_necessity[i][j].ToString() + "Y" + (j + 1).ToString() + " + ";
                    }
                    left = left.Substring(0, left.Length - 2);
                    for(int j = 0; j < _m; j++)
                    {
                        right += (_matrix_ram[i][j] * _matrix_binary[k][j]).ToString() + " + ";
                    }
                    right = right.Substring(0, right.Length - 2);
                    res.Add(left + " <= " + right);
                }
                res.Add("--------------------");
            }
            return res;
            */
            return null;
        }

        public List<double> getV1(int i)
        {
            List<double> v = new List<double>();
            for (int k = 0; k < _np; k++)
            {
                double y = 0;
                for (int j = 0; j < _m; j++)
                {
                    y += (getD(_D[k, 0], _D[k, 1]) * _P[k, 0] - _C[k, 0]) * _contract[k, j] * _binary[i, j];
                }
                v.Add(y);
            }
            return v;
        }

        public double getD(double mu, double sigma)
        {
            if (sigma == 4.5)
            {
                return 3.21;
            }
            else if (sigma == 5.75)
            {
                return 3.01;
            }
            return 0;
        }

        public List<List<double>> getSubjects(int i)
        {
            List<List<double>> val = new List<List<double>>();
            Vector<double> e = _binary.Row(i);
            Matrix<double> r = Matrix<double>.Build.Dense(_np, 1);
            for (int j = 0; j < _np; j++)
            {
                Vector<double> v = _A.Row(j);
                Vector<double> b = _B.Row(j);
                double ri = 0;
                for (int k = 0; k < _m; k++)
                {
                    ri += b[k] * e[k];
                }
                r[j, 0] = ri;
                List<double> line = v.ToList();
                line.Add(ri);
                val.Add(line);
            }
            Matrix<double> yVal = _A.Inverse().Multiply(r);
            val.Add(yVal.Transpose().Row(0).ToList());
            return val;
        }

        public static void generateBinary(char[] str, int index)
        {
            if (index == str.Length)
            {
                _binary_tmp.Add(new string(str));
                return;
            }

            if (str[index] == '?')
            {
                str[index] = '1';
                generateBinary(str, index + 1);

                str[index] = '0';
                generateBinary(str, index + 1);

                str[index] = '?';
            }
            else
                generateBinary(str, index + 1);
        }
    }
}
