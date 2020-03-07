using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace WcfService
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Service1" in both code and config file together.
    //[ServiceBehavior(InstanceContextMode = InstanceContextMode.Single)]
    [ServiceBehavior(InstanceContextMode = InstanceContextMode.PerSession)]
    public class Service1 : IService1
    {

        private MyAlgorithm algorithm;
        private int status = 0;
        private int index;
        private string RestrictionStr = "";
        private string MaxStr = "";
        private string ResultStr = "";
        private double maxValue;
        private List<double> v;

        public string SetCalcInstance(MyAlgorithm algorithm)
        {
            try
            {
                this.algorithm = algorithm;
            }
            catch
            {
                return "failed";
            }
            
            return "success";
        }

        public string CreateCalcInstance(int product, int player, int necessity)
        {
            try
            {
                this.algorithm = new MyAlgorithm(product, player, product);
            }
            catch
            {
                return "failed";
            }
            return "success";            
        }

        public void CalculateResult(int index)
        {
            status = 1;
            this.index = index;
        }

        public double GetResult()
        {
            status = 0;
            return maxValue;
        }

        public int GetStatus()
        {
            return status;
        }

        public void AssignMatrix(string lines, int type)
        {
            algorithm.Assign(lines, type);
        }

        public string GetRestrictionString()
        {
            
            string tmp = "";
            List<List<double>> lines = algorithm.getSubjects(index);
            
            for (int l = 0; l < lines.Count() - 1; l++)
            {
                var item = lines[l];
                tmp = "";
                for (int j = 0; j < item.Count() - 1; j++)
                {
                    tmp += item[j].ToString("0.##") + "Y" + (j + 1).ToString() + " + ";
                }
                tmp = tmp.Substring(0, tmp.Length - 2);
                tmp += " <= " + item[item.Count() - 1].ToString("0.##");

                RestrictionStr += tmp + "\n";
            }

            RestrictionStr += "\n--------------------------\n";

            status = 2;

            return RestrictionStr;
        }

        public string GetMaxString()
        {
            v = algorithm.getV1(index);
            string vStr = "";
            for (int i = 0; i < v.Count(); i++)
            {
                vStr += v[i].ToString("0.##") + "Y" + (i + 1).ToString() + " + ";
            }
            vStr = "v = max(" + vStr.Substring(0, vStr.Length - 2) + ")";
            MaxStr = vStr;
            MaxStr += "\n------------------------\n";
            status = 3;
            return MaxStr;
        }

        public string GetMaxValue()
        {
            string tmp = "";
            List<List<double>> lines = algorithm.getSubjects(index);
            List<double> yResult = lines[lines.Count() - 1];
            double max = 0;
            for (int j = 0; j < yResult.Count(); j++)
            {
                tmp += "Y" + (j + 1).ToString() + " = " + yResult[j].ToString("0.##") + " , ";
                max += yResult[j] * v[j];
            }
            tmp = tmp.Substring(0, tmp.Length - 2);

            maxValue = max;

            string result = "";
            result += "\n---------------------------\n";
            result += tmp;
            result += "max = " + max.ToString("0.##");
            result += "\n-------------------------------------------\n";

            status = 4;

            return result;
        }
    }
}
